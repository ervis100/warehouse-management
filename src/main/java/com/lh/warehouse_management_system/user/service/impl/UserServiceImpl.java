package com.lh.warehouse_management_system.user.service.impl;

import com.lh.warehouse_management_system.auth.dto.RegisterRequest;
import com.lh.warehouse_management_system.auth.service.AuthenticationService;
import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.common.mapper.ModelMapper;
import com.lh.warehouse_management_system.user.dto.UserResponseDTO;
import com.lh.warehouse_management_system.user.dto.UserUpdateDTO;
import com.lh.warehouse_management_system.user.model.Role;
import com.lh.warehouse_management_system.user.model.User;
import com.lh.warehouse_management_system.user.repository.RoleRepository;
import com.lh.warehouse_management_system.user.repository.UserRepository;
import com.lh.warehouse_management_system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequest request) {
        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(()->new DatabaseEntityNotFoundException("Role"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .deleted(false)
                .roles(List.of(role))
                .build();
        return userRepository.save(user);
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepository.findAllByDeletedFalse().stream().map(ModelMapper::userToResponseDto).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id).map(ModelMapper::userToResponseDto)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("User"));
    }

    @Override
    public void updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("User"));

        Role role = roleRepository.findById(userUpdateDTO.getRole())
                .orElseThrow(() -> new DatabaseEntityNotFoundException("Role"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setFirstname(userUpdateDTO.getFirstname());
        user.setLastname(userUpdateDTO.getLastname());
        user.setEmail(userUpdateDTO.getEmail());
        user.setUsername(userUpdateDTO.getUsername());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DatabaseEntityNotFoundException("User"));

        user.setDeleted(true);
        userRepository.save(user);
    }
}
