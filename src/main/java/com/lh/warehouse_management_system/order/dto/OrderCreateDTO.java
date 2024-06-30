package com.lh.warehouse_management_system.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateDTO {

    @NotNull
    private LocalDate deadLine;

    @NotEmpty
    @Valid
    private Set<OrderItemRequestDTO> items;
}
