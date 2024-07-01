package com.lh.warehouse_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/auth/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/api-docs",
            "/docs",
            "/docs/swagger-config"

    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(HttpMethod.GET, "api/orders").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers(HttpMethod.GET, "api/orders/*/details").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers(HttpMethod.POST, "api/orders").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.GET, "api/orders/client").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.PUT, "api/orders/*").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.POST, "api/orders/*/cancel").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.POST, "api/orders/*/submit").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.POST, "api/orders/*/approve").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers(HttpMethod.POST, "api/orders/*/decline").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers(HttpMethod.GET, "api/inventory/items").hasAnyRole("CLIENT", "WAREHOUSE_MANAGER")
                                .requestMatchers("api/inventory/items/**").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers("api/deliveries/**").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers("api/trucks/**").hasRole("WAREHOUSE_MANAGER")
                                .requestMatchers("api/users/**").hasRole("SYSTEM_ADMIN")
                                .requestMatchers("api/auth/change-password").authenticated()
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}