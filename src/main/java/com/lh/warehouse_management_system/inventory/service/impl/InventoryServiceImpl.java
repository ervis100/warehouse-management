package com.lh.warehouse_management_system.inventory.service.impl;

import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.common.mapper.ModelMapper;
import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.dto.ItemCreateDto;
import com.lh.warehouse_management_system.inventory.dto.ItemResponseDto;
import com.lh.warehouse_management_system.inventory.dto.ItemUpdateDto;
import com.lh.warehouse_management_system.inventory.repository.ItemRepository;
import com.lh.warehouse_management_system.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemResponseDto> getItems() {
        return itemRepository.findAll().stream().map(ModelMapper::itemToResponseDto).collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Item"));
        return ModelMapper.itemToResponseDto(item);
    }

    @Override
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {
        Item item = itemRepository.save(ModelMapper.itemDtoToItem(itemCreateDto));
        return ModelMapper.itemToResponseDto(item);
    }

    @Override
    public void updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Item"));

        item.setName(itemUpdateDto.getName());
        item.setQuantity(itemUpdateDto.getQuantity());
        item.setUnitPrice(itemUpdateDto.getUnitPrice());
        itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

}
