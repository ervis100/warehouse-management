package com.lh.warehouse_management_system.inventory.service;

import com.lh.warehouse_management_system.inventory.dto.ItemCreateDto;
import com.lh.warehouse_management_system.inventory.dto.ItemResponseDto;
import com.lh.warehouse_management_system.inventory.dto.ItemUpdateDto;

import java.util.List;

public interface InventoryService {
    List<ItemResponseDto> getItems();

    ItemResponseDto getItemById(Long id);

    ItemResponseDto createItem(ItemCreateDto itemCreateDto);

    void updateItem(Long id, ItemUpdateDto itemUpdateDto);

    void deleteItem(Long id);
}
