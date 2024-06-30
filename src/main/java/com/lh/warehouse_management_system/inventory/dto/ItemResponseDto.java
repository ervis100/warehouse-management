package com.lh.warehouse_management_system.inventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDto {

    private Long id;

    private String name;

    private Long quantity;

    private Double unitPrice;
}
