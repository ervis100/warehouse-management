package com.lh.warehouse_management_system.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequest {

    @Size(min = 6, max = 20)
    private String currentPassword;

    @Size(min = 6, max = 20)
    private String newPassword;

    @Size(min = 6, max = 20)
    private String confirmPassword;
}
