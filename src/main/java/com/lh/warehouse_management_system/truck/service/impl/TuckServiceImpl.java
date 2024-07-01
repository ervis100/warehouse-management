package com.lh.warehouse_management_system.truck.service.impl;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.common.exception.DuplicateException;
import com.lh.warehouse_management_system.common.mapper.ModelMapper;
import com.lh.warehouse_management_system.truck.Truck;
import com.lh.warehouse_management_system.truck.dto.TruckCreateDTO;
import com.lh.warehouse_management_system.truck.dto.TruckResponseDTO;
import com.lh.warehouse_management_system.truck.dto.TruckUpdateDTO;
import com.lh.warehouse_management_system.truck.repository.TruckRepository;
import com.lh.warehouse_management_system.truck.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    @Override
    public TruckResponseDTO createTruck(TruckCreateDTO truckCreateDTO) {
        truckCreateDTO.setLicensePlate(truckCreateDTO.getLicensePlate().toUpperCase());
        truckCreateDTO.setChassisNumber(truckCreateDTO.getChassisNumber().toUpperCase());

        List<Truck> existingTruck = truckRepository.findByChassisNumberOrLicensePlate(truckCreateDTO.getChassisNumber(), truckCreateDTO.getLicensePlate());
        if (!existingTruck.isEmpty()) {
            throw new DuplicateException("Truck", "Chassis number or license plate");
        }

        Truck truck = truckRepository.save(ModelMapper.truckDtoToTruck(truckCreateDTO));
        return ModelMapper.truckToResponseDto(truck);
    }

    @Override
    public TruckResponseDTO getTruckById(Long id) {
        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Truck"));
        return ModelMapper.truckToResponseDto(truck);
    }

    @Override
    public List<TruckResponseDTO> getTrucks() {
        return truckRepository.findAll().stream().map(ModelMapper::truckToResponseDto).collect(Collectors.toList());
    }

    @Override
    public void updateTruck(Long id, TruckUpdateDTO truckUpdateDTO) {
        truckUpdateDTO.setLicensePlate(truckUpdateDTO.getLicensePlate().toUpperCase());
        truckUpdateDTO.setChassisNumber(truckUpdateDTO.getChassisNumber().toUpperCase());

        Truck truckToUpdate = truckRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Truck"));

        Optional<Truck> existingLicensePlate = truckRepository.findByLicensePlate(truckUpdateDTO.getLicensePlate());
        if (existingLicensePlate.isPresent() && !Objects.equals(existingLicensePlate.get().getId(), id)) {
            throw new DuplicateException("Truck", "License plate");
        }

        Optional<Truck> existingChassisNumber = truckRepository.findByChassisNumber(truckUpdateDTO.getChassisNumber());
        if (existingChassisNumber.isPresent() && !Objects.equals(existingLicensePlate.get().getId(), id)) {
            throw new DuplicateException("Truck", "Chassis number");
        }

        truckToUpdate.setChassisNumber(truckUpdateDTO.getChassisNumber());
        truckToUpdate.setLicensePlate(truckUpdateDTO.getLicensePlate());
        truckRepository.save(truckToUpdate);
    }

    @Override
    public void deleteTruck(Long id) {
        truckRepository.deleteById(id);
    }

}
