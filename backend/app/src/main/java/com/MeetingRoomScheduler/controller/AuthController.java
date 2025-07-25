package com.MeetingRoomScheduler.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.MeetingRoomScheduler.domain.user.User;
import com.MeetingRoomScheduler.dto.request.LoginRequest;
import com.MeetingRoomScheduler.dto.request.SignUpRequest;
import com.MeetingRoomScheduler.dto.response.AuthResponse;
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
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authenticateAndGetToken(loginRequest.username(), loginRequest.password());
        return new AuthResponse(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserWithUsername(signUpRequest.username())) {
            throw new DuplicatedUserInfoException(
                    String.format("Username %s already been used", signUpRequest.username()));
        }
        if (userService.hasUserWithEmail(signUpRequest.email())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
        }

        userService.saveUser(this.mapSignUpRequestToUser(signUpRequest));

        String token = authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
        return new AuthResponse(token);
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