package com.lh.warehouse_management_system.truck;

import com.lh.warehouse_management_system.truck.dto.TruckCreateDTO;
import com.lh.warehouse_management_system.truck.dto.TruckResponseDTO;
import com.lh.warehouse_management_system.truck.dto.TruckUpdateDTO;
import com.lh.warehouse_management_system.truck.service.TruckService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/trucks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TruckController {

    private final TruckService truckService;

    @GetMapping
    public ResponseEntity<List<TruckResponseDTO>> getAllTrucks() {
        return new ResponseEntity<>(truckService.getTrucks(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addTruck(@RequestBody @Valid TruckCreateDTO truckCreateDTO) {
        TruckResponseDTO truck = truckService.createTruck(truckCreateDTO);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(truck.getId())
                                .toUri())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TruckResponseDTO> getTruckById(@PathVariable Long id) {
        return new ResponseEntity<>(truckService.getTruckById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTruck(@PathVariable Long id, @RequestBody @Valid TruckUpdateDTO truckUpdateDTO) {
        truckService.updateTruck(id, truckUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTruck(@PathVariable Long id) {
        truckService.deleteTruck(id);
        return ResponseEntity.noContent().build();
    }

}
