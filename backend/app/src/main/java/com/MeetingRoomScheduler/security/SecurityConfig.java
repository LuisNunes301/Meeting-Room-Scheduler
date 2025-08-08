package com.MeetingRoomScheduler.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

        private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                                .requestMatchers(HttpMethod.GET, "/api/users/me")
                                                .hasAnyAuthority(ADMIN, USER)
                                                .requestMatchers("/api/users", "/api/users/**")
                                                .permitAll()
                                                .requestMatchers("/api/rooms", "/api/rooms/**")
                                                .hasAnyAuthority(ADMIN, USER)
                                                .requestMatchers("/api/reservations", "/api/reservations/**")
                                                .hasAnyAuthority(ADMIN, USER)
                                                .requestMatchers("/public/**", "/auth/**")
                                                .permitAll()
                                                .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html",
                                                                "/swagger-ui/**", "/v3/api-docs",
                                                                "/v3/api-docs/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtCookieAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .sessionManagement(
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .csrf(AbstractHttpConfigurer::disable)
                                .build();
        }

        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
}