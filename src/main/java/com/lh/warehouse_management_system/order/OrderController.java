package com.lh.warehouse_management_system.order;

import com.lh.warehouse_management_system.order.dto.*;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import com.lh.warehouse_management_system.order.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateDTO orderCreateDTO) {
        OrderResponseDto orderResponseDto = orderService.createOrder(orderCreateDTO);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(orderResponseDto.getId())
                                .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam(required = false) OrderStatus status) {
        return new ResponseEntity<>(orderService.getOrders(status), HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<OrderDetailedResponseDTO> getOrderDetails(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderDetails(id), HttpStatus.OK);
    }

    @GetMapping("/client")
    public ResponseEntity<List<OrderResponseDto>> getClientOrders(@RequestParam(required = false) OrderStatus status) {
        return new ResponseEntity<>(orderService.getClientOrders(status), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderUpdateDto orderUpdateDto) {
        orderService.updateOrder(id, orderUpdateDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitOrder(@PathVariable Long id) {
        orderService.submitOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveOrder(@PathVariable Long id) {
        orderService.approveOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<?> declineOrder(@PathVariable Long id, @RequestBody DeclineOrderRequest declineOrderRequest) {
        orderService.declineOrder(id, declineOrderRequest);
        return ResponseEntity.ok().build();
    }

}
