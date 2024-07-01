package com.lh.warehouse_management_system.common.exception;

import com.lh.warehouse_management_system.auth.exception.InvalidCredentialsException;
import com.lh.warehouse_management_system.auth.exception.NewPasswordDontMatchException;
import com.lh.warehouse_management_system.auth.exception.SamePasswordChangeException;
import com.lh.warehouse_management_system.delivery.exception.DeliveryDayException;
import com.lh.warehouse_management_system.delivery.exception.MaximumDeliveryCapacityExceeded;
import com.lh.warehouse_management_system.order.exception.InsufficientItemsException;
import com.lh.warehouse_management_system.order.exception.InvalidDeadlineException;
import com.lh.warehouse_management_system.order.exception.InvalidOrderStatusException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {
    private final MessageSource messageSource;

    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ExceptionResponse errorDTO = ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.toString())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(
                        exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> ExceptionDetails.builder()
                                        .detail(StringUtils.capitalize(error.getDefaultMessage()))
                                        .message(StringUtils.capitalize("Validation Error"))
                                        .code(error.getField()).build()).toList())
                .build();
        log.error(exception.getMessage());
        return errorDTO;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.BAD_REQUEST.getReasonPhrase())).code(HttpStatus.BAD_REQUEST.getReasonPhrase()).detail(HttpStatus.BAD_REQUEST.getReasonPhrase()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handle(Exception exception, HttpServletRequest request) {
        log.error("Unknown exception in exception handler: ", exception);
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DatabaseEntityNotFoundException.class)
    public ExceptionResponse handle(DatabaseEntityNotFoundException exception, HttpServletRequest request) {
        String message = StringUtils.capitalize(exception.getMessage());
        log.error(message);
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(
                        List.of(ExceptionDetails.builder()
                                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(message)
                                .detail("")
                                .build()
                        )
                ).build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ExceptionResponse handle(DuplicateException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.CONFLICT.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InsufficientItemsException.class)
    public ExceptionResponse handle(InsufficientItemsException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.CONFLICT.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ExceptionResponse handle(InvalidOrderStatusException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.CONFLICT.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeliveryDayException.class)
    public ExceptionResponse handle(DeliveryDayException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaximumDeliveryCapacityExceeded.class)
    public ExceptionResponse handle(MaximumDeliveryCapacityExceeded exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ExceptionResponse handle(InvalidCredentialsException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewPasswordDontMatchException.class)
    public ExceptionResponse handle(NewPasswordDontMatchException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDeadlineException.class)
    public ExceptionResponse handle(InvalidDeadlineException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SamePasswordChangeException.class)
    public ExceptionResponse handle(SamePasswordChangeException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return ExceptionResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI().substring(request.getContextPath().length()))
                .errors(List.of(ExceptionDetails.builder().message(StringUtils.capitalize(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())).code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).detail(exception.getMessage()).build()))
                .build();
    }
}