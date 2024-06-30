package com.lh.warehouse_management_system.user.dto;

import com.lh.warehouse_management_system.user.model.Role;
import com.lh.warehouse_management_system.user.model.Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String username;

    private List<RoleResponseDto> roles;

}
