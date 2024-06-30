package com.lh.warehouse_management_system.truck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TruckUpdateDTO {

    @Size(min = 17, max = 17)
    @NotBlank
    private String chassisNumber;

    @Size(min = 3, max = 7)
    @NotBlank
    private String licensePlate;
}
