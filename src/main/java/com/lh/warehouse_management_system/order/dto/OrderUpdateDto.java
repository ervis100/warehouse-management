package com.lh.warehouse_management_system.order.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateDto {

    @NotEmpty
    Set<OrderItemRequestDTO> items;

}
