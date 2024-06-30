package com.lh.warehouse_management_system.config;

import com.lh.warehouse_management_system.auth.service.AuthenticationService;
import com.lh.warehouse_management_system.auth.dto.RegisterRequest;
import com.lh.warehouse_management_system.common.exception.DatabaseEntityNotFoundException;
import com.lh.warehouse_management_system.user.model.Role;
import com.lh.warehouse_management_system.user.model.User;
import com.lh.warehouse_management_system.user.repository.RoleRepository;
import com.lh.warehouse_management_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = repository.findByUsername(username)
                    .orElseThrow(() -> new DatabaseEntityNotFoundException("User"));
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner init(RoleRepository repository, UserRepository userRepository, AuthenticationService authenticationService) {
        return args -> {
            if (repository.count() == 0) {
                List<Role> roles = List.of(
                        new Role(1L, "ROLE_CLIENT"),
                        new Role(2L, "ROLE_WAREHOUSE_MANAGER"),
                        new Role(3L, "ROLE_SYSTEM_ADMIN"));
                repository.saveAll(roles);
            }

            if (userRepository.count() == 0) {
                authenticationService.register(
                        RegisterRequest.builder()
                                .firstname("client")
                                .lastname("lh")
                                .email("client@gmail.com")
                                .username("clientUser")
                                .password("password")
                                .role(1L)
                                .build());

                authenticationService.register(
                        RegisterRequest.builder()
                                .firstname("manager")
                                .lastname("lh")
                                .email("manager@gmail.com")
                                .username("managerUser")
                                .password("password")
                                .role(2L)
                                .build());

                authenticationService.register(
                        RegisterRequest.builder()
                                .firstname("system")
                                .lastname("admin")
                                .email("system.admin@gmail.com")
                                .username("systemAdmin")
                                .password("password")
                                .role(3L)
                                .build());
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
