package com.lh.warehouse_management_system.common.mapper;

import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.inventory.dto.ItemCreateDto;
import com.lh.warehouse_management_system.inventory.dto.ItemResponseDto;
import com.lh.warehouse_management_system.order.dto.OrderCreateDTO;
import com.lh.warehouse_management_system.order.dto.OrderDetailedResponseDTO;
import com.lh.warehouse_management_system.order.dto.OrderItemResponseDTO;
import com.lh.warehouse_management_system.order.dto.OrderResponseDto;
import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import com.lh.warehouse_management_system.truck.Truck;
import com.lh.warehouse_management_system.truck.dto.TruckCreateDTO;
import com.lh.warehouse_management_system.truck.dto.TruckResponseDTO;
import com.lh.warehouse_management_system.user.dto.RoleResponseDto;
import com.lh.warehouse_management_system.user.dto.UserResponseDTO;
import com.lh.warehouse_management_system.user.model.User;

import java.util.Optional;
import java.util.stream.Collectors;

public class ModelMapper {

    public static Truck truckDtoToTruck(TruckCreateDTO truckDto) {
        return Truck.builder()
                .chassisNumber(truckDto.getChassisNumber())
                .licensePlate(truckDto.getLicensePlate())
                .build();
    }

    public static TruckResponseDTO truckToResponseDto(Truck truck) {
        return TruckResponseDTO.builder()
                .id(truck.getId())
                .chassisNumber(truck.getChassisNumber())
                .licensePlate(truck.getLicensePlate())
                .build();
    }

    public static ItemResponseDto itemToResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .build();
    }

    public static Item itemDtoToItem(ItemCreateDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .unitPrice(itemDto.getUnitPrice())
                .quantity(itemDto.getQuantity())
                .build();
    }

    public static Order orderCreateDtoToEntity(OrderCreateDTO orderCreateDTO) {
        return Order.builder()
                .status(OrderStatus.CREATED)
                .deadLine(orderCreateDTO.getDeadLine())
                .build();
    }

    public static OrderResponseDto orderToDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .deadLine(order.getDeadLine())
                .submittedDate(order.getSubmittedDate())
                .items(
                        order.getItems().stream()
                                .map(orderItem -> new OrderItemResponseDTO(
                                        orderItem.getQuantity(),
                                        orderItem.getItem().getName(),
                                        orderItem.getItem().getId()))
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public static OrderDetailedResponseDTO orderToDetailedDTO(Order order, Optional<User> createdBy, Optional<User> updatedBy) {
        return OrderDetailedResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .deadLine(order.getDeadLine())
                .submittedDate(order.getSubmittedDate())
                .items(
                        order.getItems().stream()
                                .map(orderItem -> new OrderItemResponseDTO(
                                        orderItem.getQuantity(),
                                        orderItem.getItem().getName(),
                                        orderItem.getItem().getId()))
                                .collect(Collectors.toList())
                )
                .declineReason(order.getOrderDetails().getDeclineReason())
                .createdBy(createdBy.map(User::getUsername).orElse(null))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .updatedBy(updatedBy.map(User::getUsername).orElse(null))
                .build();
    }

    public static UserResponseDTO userToResponseDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(role -> RoleResponseDto.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
