package com.lh.warehouse_management_system.truck.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TruckResponseDTO {

    private Long id;

    private String chassisNumber;

    private String licensePlate;
}
