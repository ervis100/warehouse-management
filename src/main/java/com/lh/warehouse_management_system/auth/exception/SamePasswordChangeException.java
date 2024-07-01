package com.lh.warehouse_management_system.auth.exception;

public class SamePasswordChangeException extends RuntimeException {
    public SamePasswordChangeException() {
        super("New password is the same as old password");
    }
}
