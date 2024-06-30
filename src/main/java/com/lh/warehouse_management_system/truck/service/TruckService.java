package com.lh.warehouse_management_system.truck.service;

import com.lh.warehouse_management_system.truck.Truck;
import com.lh.warehouse_management_system.truck.dto.TruckCreateDTO;
import com.lh.warehouse_management_system.truck.dto.TruckResponseDTO;
import com.lh.warehouse_management_system.truck.dto.TruckUpdateDTO;

import java.util.List;

public interface TruckService {

    TruckResponseDTO createTruck(TruckCreateDTO truckCreateDTO);

    Truck getTruckById(Long id);

    List<TruckResponseDTO> getTrucks();

    void updateTruck(Long id, TruckUpdateDTO truckUpdateDTO);

    void deleteTruck(Long id);
}
