package com.lh.warehouse_management_system.order.service.impl;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.repository.ItemRepository;
import com.lh.warehouse_management_system.order.dto.OrderCreateDTO;
import com.lh.warehouse_management_system.order.dto.OrderItemRequestDTO;
import com.lh.warehouse_management_system.order.dto.OrderResponseDto;
import com.lh.warehouse_management_system.order.exception.InsufficientItemsException;
import com.lh.warehouse_management_system.order.exception.InvalidDeadlineException;
import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.order.model.OrderItem;
import com.lh.warehouse_management_system.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderCreateDTO orderCreateDTO;

    private OrderCreateDTO insufficientItemsDTO;

    @BeforeEach
    void setUp() {
        // Initialize OrderCreateDTO with valid data
        orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setDeadLine(LocalDate.now().plusDays(1));
        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO();
        orderItemRequestDTO.setItemId(1L);
        orderItemRequestDTO.setQuantity(2);
        orderCreateDTO.setItems(Set.of(orderItemRequestDTO));

        insufficientItemsDTO = new OrderCreateDTO();
        insufficientItemsDTO.setDeadLine(LocalDate.now().plusDays(2));
        OrderItemRequestDTO orderItemRequestInsufficientDTO = new OrderItemRequestDTO();
        orderItemRequestInsufficientDTO.setItemId(1L);
        orderItemRequestInsufficientDTO.setQuantity(60);
        insufficientItemsDTO.setItems(Set.of(orderItemRequestInsufficientDTO));
    }

    @Test
    void testCreateOrderSuccess() {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(50L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDto response = orderService.createOrder(orderCreateDTO);

        assertNotNull(response);
        verify(itemRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testInsufficientItemsForOrder() {
        Item item = new Item();
        item.setId(1L);
        item.setQuantity(50L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(InsufficientItemsException.class, () -> {
            orderService.createOrder(insufficientItemsDTO);
        });
    }

    @Test
    void testCreateOrderWithInvalidDeadline() {
        orderCreateDTO.setDeadLine(LocalDate.now().minusDays(1));

        assertThrows(InvalidDeadlineException.class, () -> {
            orderService.createOrder(orderCreateDTO);
        });
    }

    @Test
    void testCreateOrderWithNonExistentItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DatabaseEntityNotFoundException.class, () -> {
            orderService.createOrder(orderCreateDTO);
        });
    }
}
