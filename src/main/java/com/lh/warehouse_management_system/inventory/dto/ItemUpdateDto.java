package com.lh.warehouse_management_system.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemUpdateDto {

    @NotBlank
    private String name;

    @NotNull
    private Long quantity;

    @NotNull
    private Double unitPrice;
}
