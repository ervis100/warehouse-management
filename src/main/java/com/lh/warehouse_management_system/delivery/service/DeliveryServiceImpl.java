package com.lh.warehouse_management_system.delivery.service;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.delivery.Delivery;
import com.lh.warehouse_management_system.delivery.dto.DeliveryRequest;
import com.lh.warehouse_management_system.delivery.exception.DeliveryDayException;
import com.lh.warehouse_management_system.delivery.repository.DeliveryRepository;
import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.repository.ItemRepository;
import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.order.model.OrderItem;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import com.lh.warehouse_management_system.order.exception.InvalidOrderStatusException;
import com.lh.warehouse_management_system.order.repository.OrderRepository;
import com.lh.warehouse_management_system.truck.Truck;
import com.lh.warehouse_management_system.truck.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final TruckRepository truckRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Delivery scheduleDelivery(DeliveryRequest deliveryRequest) {
        if (deliveryRequest.getDeliveryDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new DeliveryDayException(DayOfWeek.SUNDAY);
        }

        Set<Truck> trucks = deliveryRequest.getTrucks().stream().map(truckId -> {
            return truckRepository.findById(truckId)
                    .orElseThrow(() -> new DatabaseEntityNotFoundException("Truck"));
        }).collect(Collectors.toSet());

        List<Delivery> deliveriesOnDate = deliveryRepository.findByDeliveryDate(deliveryRequest.getDeliveryDate());
        for (Truck truck : trucks) {
            long truckDeliveriesCount = deliveriesOnDate.stream()
                    .filter(delivery -> delivery.getTrucks().stream().anyMatch(deliveryTruck -> deliveryTruck.getId().equals(truck.getId())))
                    .count();
            if (truckDeliveriesCount >= 1) {
                throw new RuntimeException("Truck with ID " + truck.getId() + " is already scheduled for a delivery on " + deliveryRequest.getDeliveryDate());
            }
        }

        Integer maxDeliveryCapacity = trucks.size() * 10;
        AtomicReference<Integer> itemsCount = new AtomicReference<>(0);

        Set<Order> orders = deliveryRequest.getOrders().stream()
                .map(deliveryOrder -> {
                    Order order = orderRepository.findById(deliveryOrder)
                            .orElseThrow(() -> new DatabaseEntityNotFoundException("Order"));

                    if (order.getStatus() != OrderStatus.APPROVED) {
                        throw new InvalidOrderStatusException(order.getStatus());
                    }

                    for (OrderItem orderItem : order.getItems()) {
                        itemsCount.set(itemsCount.get() + orderItem.getQuantity());
                    }
                    return order;
                })
                .collect(Collectors.toSet());

        if (itemsCount.get() > maxDeliveryCapacity) {
            throw new RuntimeException("Not enough capacity in trucks for delivery");
        }

        Delivery delivery = Delivery.builder()
                .deliveryDate(deliveryRequest.getDeliveryDate())
                .trucks(new HashSet<>(truckRepository.findAllById(deliveryRequest.getTrucks())))
                .orders(orders)
                .build();

        Delivery savedDelivery = deliveryRepository.save(delivery);

        for (Order order : orders) {
            order.setStatus(OrderStatus.UNDER_DELIVERY);
            order.setDelivery(delivery);

            for (OrderItem orderItem : order.getItems()) {
                Item item = orderItem.getItem();
                item.setQuantity(item.getQuantity() - orderItem.getQuantity());
                itemRepository.save(item);
            }
            orderRepository.save(order);
        }

        return savedDelivery;
    }

    @Override
    public void fulfillDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Delivery"));
        Set<Order> orders = delivery.getOrders();
        orders.forEach(order -> order.setStatus(OrderStatus.FULFILLED));
        orderRepository.saveAll(orders);
    }
}
