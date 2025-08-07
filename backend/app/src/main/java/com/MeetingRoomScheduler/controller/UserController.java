package com.MeetingRoomScheduler.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MeetingRoomScheduler.dto.response.UserResponse;
import com.MeetingRoomScheduler.entities.user.CustomUserDetails;
import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.entities.user.UserDto;
import com.MeetingRoomScheduler.service.UserService;

import java.util.List;
import java.util.stream.Collectors;
import static com.MeetingRoomScheduler.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return UserDto.from(userService.validateAndGetUserByUsername(currentUser.getUsername()));
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        return UserDto.from(userService.validateAndGetUserByUsername(username));
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable String username) {
        User user = userService.validateAndGetUserByUsername(username);
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

}