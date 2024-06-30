package com.lh.warehouse_management_system.delivery.service;

import com.lh.warehouse_management_system.delivery.Delivery;
import com.lh.warehouse_management_system.delivery.dto.DeliveryRequest;

public interface DeliveryService {
    Delivery scheduleDelivery(DeliveryRequest deliveryRequest);

    void fulfillDelivery(Long id);
}
