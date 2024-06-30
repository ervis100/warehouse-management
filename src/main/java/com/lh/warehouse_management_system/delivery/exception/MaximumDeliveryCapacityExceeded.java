package com.lh.warehouse_management_system.delivery.exception;

public class MaximumDeliveryCapacityExceeded extends RuntimeException {
    public MaximumDeliveryCapacityExceeded(Integer itemsCount, Integer maxCapacity) {
        super(String.format("Not enough delivery capacity of %d items. Maximum capacity: %d", itemsCount, maxCapacity));
    }
}
