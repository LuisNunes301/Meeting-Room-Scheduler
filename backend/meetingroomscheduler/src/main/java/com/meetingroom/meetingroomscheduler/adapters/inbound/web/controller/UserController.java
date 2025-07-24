package com.meetingroom.meetingroomscheduler.adapters.inbound.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meetingroom.meetingroomscheduler.domain.model.User;
import com.meetingroom.meetingroomscheduler.ports.in.UserUseCase;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userUseCase.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userUseCase.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
