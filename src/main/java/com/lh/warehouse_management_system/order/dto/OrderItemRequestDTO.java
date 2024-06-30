package com.lh.warehouse_management_system.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDTO {

    @NotNull
    private Long itemId;

    @NotNull
    @Positive(message = "Number must be greater than 0")
    private Integer quantity;
}
