package com.lh.warehouse_management_system.order.exception;

import com.lh.warehouse_management_system.order.model.OrderStatus;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(OrderStatus orderStatus) {
        super(String.format("Invalid operation for order with status: %s", orderStatus));
    }
}
