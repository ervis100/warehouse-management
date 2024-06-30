package com.lh.warehouse_management_system.auth.service;

import com.lh.warehouse_management_system.auth.dto.AuthenticationRequest;
import com.lh.warehouse_management_system.auth.dto.AuthenticationResponse;
import com.lh.warehouse_management_system.auth.dto.PasswordChangeRequest;
import com.lh.warehouse_management_system.auth.dto.RegisterRequest;
import com.lh.warehouse_management_system.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void changePassword(PasswordChangeRequest request);
}

