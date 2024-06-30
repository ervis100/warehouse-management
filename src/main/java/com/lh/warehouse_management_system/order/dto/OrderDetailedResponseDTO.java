package com.lh.warehouse_management_system.order.dto;

import com.lh.warehouse_management_system.delivery.Delivery;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailedResponseDTO {

    private Long id;

    private OrderStatus status;

    private List<OrderItemResponseDTO> items;

    private Delivery delivery;

    private String declineReason;

    private LocalDate submittedDate;

    private LocalDate deadLine;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

}
