package com.lh.warehouse_management_system.order.exception;

public class InsufficientItemsException extends RuntimeException {

    public InsufficientItemsException(String item) {
        super(String.format("Not enough %s in warehouse!", item));
    }
}
