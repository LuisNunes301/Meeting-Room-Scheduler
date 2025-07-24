package com.meetingroom.meetingroomscheduler.application.service;

import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.User;
import com.meetingroom.meetingroomscheduler.ports.in.UserUseCase;
import com.meetingroom.meetingroomscheduler.ports.out.UserRepositoryPort;

public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}