package com.lh.warehouse_management_system.order.dto;

import com.lh.warehouse_management_system.order.model.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private OrderStatus status;

    private Set<OrderItemResponseDTO> items;

    private LocalDate submittedDate;

    private LocalDate deadLine;
}
