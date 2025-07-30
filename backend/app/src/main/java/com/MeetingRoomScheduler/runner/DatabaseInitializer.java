package com.MeetingRoomScheduler.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.security.SecurityConfig;
import com.MeetingRoomScheduler.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });
        log.info("Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(
            // Long id, String username, String name, String email, String role,
            // LocalDateTime createAt,LocalDateTime updatedAt, boolean isActive

            new User("admin", "admin", "Admin", "admin@mycompany.com", SecurityConfig.ADMIN, LocalDateTime.now(), true),
            new User("user", "user", "User", "user@mycompany.com", SecurityConfig.USER, LocalDateTime.now(), true));
}
