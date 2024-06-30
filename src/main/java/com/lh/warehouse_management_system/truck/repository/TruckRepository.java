package com.lh.warehouse_management_system.truck.repository;

import com.lh.warehouse_management_system.truck.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    List<Truck> findByChassisNumberOrLicensePlate(String chassisNumber, String licensePlate);

    Optional<Truck> findByChassisNumber(String chassisNumber);

    Optional<Truck> findByLicensePlate(String licensePlate);
}
