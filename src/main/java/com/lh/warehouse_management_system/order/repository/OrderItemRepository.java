package com.lh.warehouse_management_system.order.repository;

import com.lh.warehouse_management_system.order.model.OrderItem;
import com.lh.warehouse_management_system.order.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    List<OrderItem> findAllByOrderId(Long id);
}
