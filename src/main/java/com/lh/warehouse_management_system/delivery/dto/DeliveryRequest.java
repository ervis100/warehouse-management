package com.lh.warehouse_management_system.delivery.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequest {

    @NotEmpty
    private Set<Long> trucks;

    @NotEmpty
    private List<Long> orders;

    @NotNull
    private LocalDate deliveryDate;

}
