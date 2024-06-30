package com.lh.warehouse_management_system.delivery.repository;

import com.lh.warehouse_management_system.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByDeliveryDate(LocalDate deliveryDate);
}
