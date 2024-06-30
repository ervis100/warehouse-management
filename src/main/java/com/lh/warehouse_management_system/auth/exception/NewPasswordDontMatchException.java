package com.lh.warehouse_management_system.auth.exception;

public class NewPasswordDontMatchException extends RuntimeException {
    public NewPasswordDontMatchException() {
        super("New password doesnt match password confirmation");
    }
}
