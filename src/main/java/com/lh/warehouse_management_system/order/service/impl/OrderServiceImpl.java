package com.lh.warehouse_management_system.order.service.impl;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.common.mapper.ModelMapper;
import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.repository.ItemRepository;
import com.lh.warehouse_management_system.order.OrderSpecifications;
import com.lh.warehouse_management_system.order.dto.*;
import com.lh.warehouse_management_system.order.exception.InvalidDeadlineException;
import com.lh.warehouse_management_system.order.exception.InvalidOrderStatusException;
import com.lh.warehouse_management_system.order.model.*;
import com.lh.warehouse_management_system.order.repository.OrderItemRepository;
import com.lh.warehouse_management_system.order.repository.OrderRepository;
import com.lh.warehouse_management_system.order.service.OrderService;
import com.lh.warehouse_management_system.user.model.User;
import com.lh.warehouse_management_system.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderCreateDTO orderCreateDTO) {
        if (orderCreateDTO.getDeadLine().isBefore(LocalDate.now())) {
            throw new InvalidDeadlineException();
        }

        Order order = ModelMapper.orderCreateDtoToEntity(orderCreateDTO);

        List<OrderItem> orderItems = orderCreateDTO.getItems().stream().map(
                orderItem -> {
                    Item item = itemRepository.findById(orderItem.getItemId())
                            .orElseThrow(() -> new DatabaseEntityNotFoundException("Item"));
                    return new OrderItem(order, item, orderItem.getQuantity());
                }
        ).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setOrderDetails(new OrderDetails());
        Order savedOrder = orderRepository.save(order);
        return ModelMapper.orderToDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrders(OrderStatus status) {
        Specification<Order> spec = Specification.where(OrderSpecifications.hasStatus(status))
                .and(OrderSpecifications.orderBySubmittedDateDesc());
        return orderRepository.findAll(spec).stream().map(ModelMapper::orderToDto).collect(Collectors.toList());
    }


    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));
        return ModelMapper.orderToDto(order);
    }

    @Override
    @Transactional
    public void updateOrder(Long id, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!order.getCreatedBy().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!order.getStatus().equals(OrderStatus.CREATED) && !order.getStatus().equals(OrderStatus.DECLINED)) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
        orderItems.forEach(orderItem -> {
            if (orderUpdateDto.getItems().stream()
                    .noneMatch(orderItemDTO -> orderItemDTO.getItemId().equals(orderItem.getItem().getId()))) {
                orderItemRepository.deleteById(new OrderItemId(order.getId(), orderItem.getItem().getId()));
            }
        });

        orderUpdateDto.getItems().forEach(orderItemDTO -> {
            Item item = itemRepository.findById(orderItemDTO.getItemId())
                    .orElseThrow(() -> new DatabaseEntityNotFoundException("Item"));
            OrderItem orderItem = orderItemRepository.findById(new OrderItemId(order.getId(), item.getId())).orElse(null);

            if (orderItem == null) {
                orderItemRepository.save(new OrderItem(order, item, orderItemDTO.getQuantity()));
            } else {
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItemRepository.save(orderItem);
            }
        });
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));

        List<OrderStatus> notAllowedStatus = new ArrayList<>(Arrays.asList(
                OrderStatus.CANCELED,
                OrderStatus.FULFILLED,
                OrderStatus.UNDER_DELIVERY));

        if (notAllowedStatus.contains(order.getStatus())) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public void submitOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));

        List<OrderStatus> allowedStatus = new ArrayList<>(Arrays.asList(
                OrderStatus.CREATED,
                OrderStatus.DECLINED));

        if (allowedStatus.contains(order.getStatus())) {
            order.setSubmittedDate(LocalDate.now());
            order.setStatus(OrderStatus.AWAITING_APPROVAL);
            orderRepository.save(order);
        } else {
            throw new InvalidOrderStatusException(order.getStatus());
        }
    }

    @Override
    public void approveOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));
        if (!order.getStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
    }

    @Override
    public void declineOrder(Long id, DeclineOrderRequest declineOrderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));
        if (!order.getStatus().equals(OrderStatus.AWAITING_APPROVAL)) {
            throw new InvalidOrderStatusException(order.getStatus());
        }

        order.setStatus(OrderStatus.DECLINED);
        OrderDetails orderDetails = order.getOrderDetails();
        orderDetails.setDeclineReason(declineOrderRequest.getDeclineReason());
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDto> getClientOrders(OrderStatus status) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Specification<Order> spec = Specification.where(OrderSpecifications.hasStatus(status)).and(OrderSpecifications.orderBySubmittedDateDesc())
                .and(OrderSpecifications.hasCustomerId(user.getId()));

        return orderRepository.findAll(spec).stream().map(ModelMapper::orderToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDetailedResponseDTO getOrderDetails(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));
        Optional<User> createdBy = userRepository.findById(order.getCreatedBy().getId());
        Optional<User> updatedBy = userRepository.findById(order.getUpdatedBy().getId());
        return ModelMapper.orderToDetailedDTO(order, createdBy, updatedBy);
    }

}
