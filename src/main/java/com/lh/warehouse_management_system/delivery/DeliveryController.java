package com.lh.warehouse_management_system.delivery;

import com.lh.warehouse_management_system.delivery.dto.DeliveryRequest;
import com.lh.warehouse_management_system.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleDelivery(@RequestBody @Valid DeliveryRequest deliveryRequest) {
        deliveryService.scheduleDelivery(deliveryRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillDelivery(@PathVariable Long id) {
        deliveryService.fulfillDelivery(id);
        return ResponseEntity.ok().build();
    }
}
