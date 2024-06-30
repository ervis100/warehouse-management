package com.lh.warehouse_management_system.order.service;

import com.lh.warehouse_management_system.order.model.OrderStatus;
import com.lh.warehouse_management_system.order.dto.*;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(OrderCreateDTO orderCreateDTO);

    List<OrderResponseDto> getClientOrders(OrderStatus status);

    OrderResponseDto getOrderById(Long id);

    List<OrderResponseDto> getOrders(OrderStatus status);

    void updateOrder(Long id, OrderUpdateDto orderUpdateDto);

    void cancelOrder(Long id);

    void submitOrder(Long id);

    void approveOrder(Long id);

    void declineOrder(Long id, DeclineOrderRequest declineOrderRequest);

    OrderDetailedResponseDTO getOrderDetails(Long id);
}
