package com.lh.warehouse_management_system.user.service;

import com.lh.warehouse_management_system.auth.dto.RegisterRequest;
import com.lh.warehouse_management_system.user.dto.UserResponseDTO;
import com.lh.warehouse_management_system.user.dto.UserUpdateDTO;
import com.lh.warehouse_management_system.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(RegisterRequest request);

    List<UserResponseDTO> getUsers();

    UserResponseDTO getUserById(Long id);

    void updateUser(Long id, UserUpdateDTO userUpdateDTO);

    void deleteUser(Long id);
}
