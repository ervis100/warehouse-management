package com.lh.warehouse_management_system.order.exception;

public class InvalidDeadlineException extends RuntimeException {
    public InvalidDeadlineException() {
        super("Invalid deadline");
    }
}
