package com.lh.warehouse_management_system.delivery.exception;

import java.time.DayOfWeek;

public class DeliveryDayException extends RuntimeException{

    public DeliveryDayException(DayOfWeek dayOfWeek) {
        super(String.format("Cannot do deliveries on %s", dayOfWeek));
    }
}
