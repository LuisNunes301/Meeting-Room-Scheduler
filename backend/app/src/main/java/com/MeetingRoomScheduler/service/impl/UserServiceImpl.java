package com.MeetingRoomScheduler.service.impl;

import org.springframework.stereotype.Service;

import com.MeetingRoomScheduler.dto.event.UserRegisteredEvent;
import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.execptions.UserNotFoundException;
import com.MeetingRoomScheduler.rabbit.userRegister.*;
import com.MeetingRoomScheduler.repository.UserRepository;
import com.MeetingRoomScheduler.service.UserService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

// @RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCreatedPublisher userCreatedPublisher;

    public UserServiceImpl(UserRepository userRepository, UserCreatedPublisher userCreatedPublisher) {
        this.userRepository = userRepository;
        this.userCreatedPublisher = userCreatedPublisher;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getName(),
                savedUser.getEmail());

        userCreatedPublisher.publish(event); // <-- Certifique-se que está sendo chamado

        return savedUser;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}