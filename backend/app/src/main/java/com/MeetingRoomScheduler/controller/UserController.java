package com.MeetingRoomScheduler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.MeetingRoomScheduler.dto.request.PasswordForgotRequest;
import com.MeetingRoomScheduler.dto.request.PasswordResetRequest;
import com.MeetingRoomScheduler.dto.request.user.UserCreateRequest;
import com.MeetingRoomScheduler.dto.request.user.UserUpdateRequest;
import com.MeetingRoomScheduler.dto.response.AuthResponse;
import com.MeetingRoomScheduler.dto.response.UserResponse;
import com.MeetingRoomScheduler.entities.user.CustomUserDetails;
import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.entities.user.UserDto;
import com.MeetingRoomScheduler.execptions.DuplicatedUserInfoException;
import com.MeetingRoomScheduler.security.SecurityConfig;
import com.MeetingRoomScheduler.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.MeetingRoomScheduler.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return UserDto.from(userService.validateAndGetUserById(currentUser.getId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @PutMapping("/me")
    public UserDto updateCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UserUpdateRequest request) {

        User updatedUser = userService.updateUser(currentUser.getId(), request, false);
        return UserDto.from(updatedUser);
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return UserDto.from(userService.validateAndGetUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        User user = userService.validateAndGetUserById(id);
        userService.deleteUser(user);
        return UserDto.from(user);
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @PutMapping("/me/password")
    public UserResponse changePassword(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {

        // Autentica com a senha atual
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        currentUser.getUsername(),
                        currentPassword));

        if (!authentication.isAuthenticated()) {
            return new UserResponse("Incorrect password.");
        }

        userService.updatePasswordByUsername(currentUser.getUsername(), newPassword);

        return new UserResponse("Password changed successfully.");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public UserResponse createUser(@RequestBody UserCreateRequest userCreateRequest) {
        if (userService.hasUserWithUsername(userCreateRequest.username())) {
            throw new DuplicatedUserInfoException(
                    String.format("Username %s already been used", userCreateRequest.username()));
        }
        if (userService.hasUserWithEmail(userCreateRequest.email())) {
            throw new DuplicatedUserInfoException(
                    String.format("Email %s already been used", userCreateRequest.email()));
        }
        userService.saveUser(this.mapSignUpRequestToUser(userCreateRequest));

        return new UserResponse("User created successfully: " + userCreateRequest.username());
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

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUserByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        User updatedUser = userService.updateUser(id, request, true);
        return UserDto.from(updatedUser);
    }

    private User mapSignUpRequestToUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setUsername(userCreateRequest.username());
        user.setPassword(passwordEncoder.encode(userCreateRequest.password()));
        user.setName(userCreateRequest.name());
        user.setEmail(userCreateRequest.email());
        user.setRole(SecurityConfig.USER);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}