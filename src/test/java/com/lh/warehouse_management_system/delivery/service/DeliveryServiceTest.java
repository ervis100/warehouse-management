package com.lh.warehouse_management_system.delivery.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.delivery.Delivery;
import com.lh.warehouse_management_system.delivery.dto.DeliveryRequest;
import com.lh.warehouse_management_system.delivery.exception.DeliveryDayException;
import com.lh.warehouse_management_system.delivery.repository.DeliveryRepository;
import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.repository.ItemRepository;
import com.lh.warehouse_management_system.order.exception.InvalidOrderStatusException;
import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.order.model.OrderItem;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import com.lh.warehouse_management_system.order.repository.OrderRepository;
import com.lh.warehouse_management_system.truck.Truck;
import com.lh.warehouse_management_system.truck.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private TruckRepository truckRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private DeliveryRequest deliveryRequest;
    Item item1;
    Item item2;

    @BeforeEach
    void setUp() {
        item1 = new Item();
        item1.setQuantity(50L);
        item1.setName("firstItem");
        item1.setId(1L);

        item2 = new Item();
        item2.setQuantity(50L);
        item2.setName("secondItem");
        item2.setId(2L);

        deliveryRequest = new DeliveryRequest();
        deliveryRequest.setDeliveryDate(LocalDate.now().plusDays(1));
        deliveryRequest.setTrucks(Set.of(1L, 2L));
        deliveryRequest.setOrders(List.of(1L, 2L));
    }

    @Test
    void testScheduleDeliverySuccess() {
        Truck truck1 = new Truck();
        truck1.setId(1L);
        Truck truck2 = new Truck();
        truck2.setId(2L);

        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck1));
        when(truckRepository.findById(2L)).thenReturn(Optional.of(truck2));

        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.APPROVED);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setItem(item1);
        orderItem1.setQuantity(5);
        orderItem1.setOrder(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.APPROVED);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setItem(item2);
        orderItem2.setQuantity(5);
        orderItem2.setOrder(order2);

        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck1));
        when(truckRepository.findById(2L)).thenReturn(Optional.of(truck2));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order2));
        when(deliveryRepository.findByDeliveryDate(any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(new Delivery());

        Delivery delivery = deliveryService.scheduleDelivery(deliveryRequest);

        assertNotNull(delivery);
        verify(truckRepository, times(2)).findById(any(Long.class));
        verify(orderRepository, times(2)).findById(any(Long.class));
        verify(deliveryRepository).save(any(Delivery.class));
    }

    @Test
    void testScheduleDeliveryOnSunday() {
        deliveryRequest.setDeliveryDate(LocalDate.now().with(DayOfWeek.SUNDAY));

        assertThrows(DeliveryDayException.class, () -> {
            deliveryService.scheduleDelivery(deliveryRequest);
        });
    }

    @Test
    void testScheduleDeliveryWithNonExistentTruck() {
        when(truckRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DatabaseEntityNotFoundException.class, () -> {
            deliveryService.scheduleDelivery(deliveryRequest);
        });
    }

    @Test
    void testScheduleDeliveryWithNonExistentOrder() {
        Truck truck = new Truck();
        truck.setId(1L);
        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck));
        when(truckRepository.findById(2L)).thenReturn(Optional.of(truck));
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(DatabaseEntityNotFoundException.class, () -> {
            deliveryService.scheduleDelivery(deliveryRequest);
        });
    }

    @Test
    void testScheduleDeliveryWithInvalidOrderStatus() {
        Truck truck = new Truck();
        truck.setId(1L);
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.FULFILLED);
        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck));
        when(truckRepository.findById(2L)).thenReturn(Optional.of(truck));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> {
            deliveryService.scheduleDelivery(deliveryRequest);
        });
    }

    @Test
    void testScheduleDeliveryWithExceedingCapacity() {
        Truck truck1 = new Truck();
        truck1.setId(1L);
        Truck truck2 = new Truck();
        truck2.setId(2L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));

        Item item1 = itemRepository.findById(1L).get();
        Item item2 = itemRepository.findById(2L).get();

        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.APPROVED);
        OrderItem orderItem1 = new OrderItem(order1,item1,20);
        order1.setItems(List.of(orderItem1));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.APPROVED);
        OrderItem orderItem2 = new OrderItem(order2,item2,20);
        order2.setItems(List.of(orderItem2));

        when(truckRepository.findById(1L)).thenReturn(Optional.of(truck1));
        when(truckRepository.findById(2L)).thenReturn(Optional.of(truck2));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order2));
        when(deliveryRepository.findByDeliveryDate(any(LocalDate.class))).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> {
            deliveryService.scheduleDelivery(deliveryRequest);
        });
    }
}
