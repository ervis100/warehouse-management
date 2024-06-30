package com.lh.warehouse_management_system.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    private Long role;
}