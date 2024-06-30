package com.lh.warehouse_management_system.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lh.warehouse_management_system.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String accessToken;

    private String refreshToken;

    private UserResponseDTO user;
}