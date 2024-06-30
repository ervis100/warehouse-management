package com.lh.warehouse_management_system.order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Integer quantity;

    private String itemName;

    private Long itemId;
}
