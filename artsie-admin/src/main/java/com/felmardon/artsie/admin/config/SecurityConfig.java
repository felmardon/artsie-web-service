package com.felmardon.artsie.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Artsie application.
 * <p>
 * - Public gallery endpoints ({@code /api/albums/**}, {@code /api/photos/**})
 *   are open to all.
 * - Admin CMS endpoints ({@code /api/admin/**}) require authentication.
 * <p>
 * Currently uses HTTP Basic auth with stateless sessions (no cookies).
 * This can be upgraded to JWT-based auth in a later phase.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Stateless API, CSRF not needed
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public gallery endpoints — open to everyone
                .requestMatchers(HttpMethod.GET, "/api/albums/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/photos/**").permitAll()
                // Health check
                .requestMatchers("/actuator/health").permitAll()
                // Everything under /api/admin/** requires authentication
                .requestMatchers("/api/admin/**").authenticated()
                // Deny everything else by default
                .anyRequest().denyAll())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
