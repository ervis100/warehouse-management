package com.lh.warehouse_management_system.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String username;

    @NotNull
    private Long role;
}
