package com.MeetingRoomScheduler.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.MeetingRoomScheduler.dto.request.LoginRequest;
import com.MeetingRoomScheduler.dto.request.PasswordForgotRequest;
import com.MeetingRoomScheduler.dto.request.PasswordResetRequest;
import com.MeetingRoomScheduler.dto.request.SignUpRequest;
import com.MeetingRoomScheduler.dto.response.AuthResponse;
import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.execptions.DuplicatedUserInfoException;
import com.MeetingRoomScheduler.security.SecurityConfig;
import com.MeetingRoomScheduler.security.TokenProvider;
import com.MeetingRoomScheduler.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/authenticate")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authenticateAndGetToken(loginRequest.username(), loginRequest.password());
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true) // true se estiver usando HTTPS
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthResponse("User authenticated successfully");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        if (userService.hasUserWithUsername(signUpRequest.username())) {
            throw new DuplicatedUserInfoException(
                    String.format("Username %s already been used", signUpRequest.username()));
        }
        if (userService.hasUserWithEmail(signUpRequest.email())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
        }

        userService.saveUser(this.mapSignUpRequestToUser(signUpRequest));

        String token = authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true) // true se for HTTPS
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new AuthResponse("User registered successfully");
    }

    @PostMapping("/forgot-password")
    public AuthResponse forgotPassword(@Valid @RequestBody PasswordForgotRequest request) {
        userService.sendPasswordForgotEmail(request.email());

        return new AuthResponse("Password forgot link sent to email.");
    }

    @PostMapping("/reset-password")
    public AuthResponse resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.token(), request.newPassword());
        return new AuthResponse("Password reset successfully.");
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authenticateAndGetToken(loginRequest.username(), loginRequest.password());
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new AuthResponse("Token refreshed successfully.");
    }

    @PostMapping("/logout")
    public AuthResponse logout(HttpServletResponse response) {
        // Clear the JWT cookie by setting it with maxAge = 0
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // This immediately expires the cookie
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new AuthResponse("User logged out successfully");
    }

    private String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }

    private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setName(signUpRequest.name());
        user.setEmail(signUpRequest.email());
        user.setRole(SecurityConfig.USER);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

}