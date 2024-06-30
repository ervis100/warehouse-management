package com.lh.warehouse_management_system.common.exception;

import lombok.Builder;

import java.util.Collection;

@Builder
public record ExceptionResponse(String statusCode,
                                String path,
                                Collection<ExceptionDetails> errors) {
}
