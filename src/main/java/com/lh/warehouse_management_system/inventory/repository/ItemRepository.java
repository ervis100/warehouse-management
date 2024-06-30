package com.lh.warehouse_management_system.inventory.repository;

import com.lh.warehouse_management_system.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
