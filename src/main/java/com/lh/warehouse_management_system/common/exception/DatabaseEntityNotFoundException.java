package com.lh.warehouse_management_system.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseEntityNotFoundException extends RuntimeException {
    private final String entity;

    public DatabaseEntityNotFoundException(String entity) {
        super(entity + " was not found");
        this.entity = entity;
    }
}
