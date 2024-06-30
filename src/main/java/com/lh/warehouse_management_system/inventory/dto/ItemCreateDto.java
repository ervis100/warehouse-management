package com.lh.warehouse_management_system.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCreateDto {

    @NotBlank
    private String name;

    @NotNull
    @Positive(message = "Number must be greater than 0")
    private Long quantity;

    @NotNull
    @Positive(message = "Number must be greater than 0")
    private Double unitPrice;
}
