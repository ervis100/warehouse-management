package com.lh.warehouse_management_system.common.exception;

import lombok.Builder;

@Builder
public record ExceptionDetails(String code,
                               String message,
                               String detail) {
}
