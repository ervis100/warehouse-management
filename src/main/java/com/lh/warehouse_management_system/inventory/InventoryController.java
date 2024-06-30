package com.lh.warehouse_management_system.inventory;

import com.lh.warehouse_management_system.inventory.dto.ItemCreateDto;
import com.lh.warehouse_management_system.inventory.dto.ItemResponseDto;
import com.lh.warehouse_management_system.inventory.dto.ItemUpdateDto;
import com.lh.warehouse_management_system.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostExchange("/items")
    public ResponseEntity<?> createItem(@RequestBody @Valid ItemCreateDto itemCreateDto) {
        ItemResponseDto item = inventoryService.createItem(itemCreateDto);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(item.getId())
                                .toUri())
                .build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getItems() {
        return new ResponseEntity<>(inventoryService.getItems(), HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long id) {
        return new ResponseEntity<>(inventoryService.getItemById(id), HttpStatus.OK);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        inventoryService.updateItem(id, itemUpdateDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }

}
