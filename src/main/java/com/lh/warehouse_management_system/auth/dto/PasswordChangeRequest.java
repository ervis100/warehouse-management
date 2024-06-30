package com.lh.warehouse_management_system.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequest {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;
}
