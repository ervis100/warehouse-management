package com.lh.warehouse_management_system.auth.service.imlp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lh.warehouse_management_system.auth.LogoutService;
import com.lh.warehouse_management_system.auth.dto.AuthenticationRequest;
import com.lh.warehouse_management_system.auth.dto.AuthenticationResponse;
import com.lh.warehouse_management_system.auth.dto.PasswordChangeRequest;
import com.lh.warehouse_management_system.auth.dto.RegisterRequest;
import com.lh.warehouse_management_system.auth.exception.InvalidCredentialsException;
import com.lh.warehouse_management_system.auth.exception.NewPasswordDontMatchException;
import com.lh.warehouse_management_system.auth.exception.SamePasswordChangeException;
import com.lh.warehouse_management_system.auth.service.AuthenticationService;
import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.common.mapper.ModelMapper;
import com.lh.warehouse_management_system.config.JwtService;
import com.lh.warehouse_management_system.user.model.Token;
import com.lh.warehouse_management_system.user.model.TokenType;
import com.lh.warehouse_management_system.user.model.User;
import com.lh.warehouse_management_system.user.repository.TokenRepository;
import com.lh.warehouse_management_system.user.repository.UserRepository;
import com.lh.warehouse_management_system.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LogoutService logoutService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = userService.createUser(request);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new DatabaseEntityNotFoundException("User"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(ModelMapper.userToResponseDto(user))
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public void changePassword(PasswordChangeRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new NewPasswordDontMatchException();
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new SamePasswordChangeException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        /*
            TODO
            logoutService.logout();
         */
        userRepository.save(user);
    }
}