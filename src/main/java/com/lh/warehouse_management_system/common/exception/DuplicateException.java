package com.lh.warehouse_management_system.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateException extends RuntimeException {

    private final String field;
    private final String entity;

    public DuplicateException(String entity, String field) {
        super(String.format("%s with this %s already exists.", entity, field));
        this.field = field;
        this.entity = entity;
    }

}
