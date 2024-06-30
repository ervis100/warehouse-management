package com.lh.warehouse_management_system.user.repository;

import com.lh.warehouse_management_system.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
