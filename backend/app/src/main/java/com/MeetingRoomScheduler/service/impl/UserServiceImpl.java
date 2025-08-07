package com.MeetingRoomScheduler.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.MeetingRoomScheduler.dto.event.PasswordForgotEvent;
import com.MeetingRoomScheduler.dto.event.UserRegisteredEvent;
import com.MeetingRoomScheduler.entities.user.User;
import com.MeetingRoomScheduler.execptions.InvalidTokenException;
import com.MeetingRoomScheduler.execptions.UserNotFoundException;
import com.MeetingRoomScheduler.rabbit.user.*;
import com.MeetingRoomScheduler.rabbit.user.passwordForgot.PasswordForgotPublisher;
import com.MeetingRoomScheduler.repository.UserRepository;
import com.MeetingRoomScheduler.security.TokenProvider;
import com.MeetingRoomScheduler.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

// @RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCreatedPublisher userCreatedPublisher;
    private final PasswordForgotPublisher passwordForgotPublisher;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserCreatedPublisher userCreatedPublisher,
            PasswordForgotPublisher passwordForgotPublisher, TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userCreatedPublisher = userCreatedPublisher;
        this.passwordForgotPublisher = passwordForgotPublisher;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public void updatePasswordByUsername(String username, String rawNewPassword) {
        User user = validateAndGetUserByUsername(username);
        user.setPassword(passwordEncoder.encode(rawNewPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Optional<Jws<Claims>> jwsOptional = tokenProvider.validateTokenAndGetJws(token);
        if (jwsOptional.isEmpty()) {
            throw new InvalidTokenException("Token inválido ou expirado.");
        }

        Jws<Claims> jws = jwsOptional.get();
        Claims claims = jws.getPayload();

        // Verifica se o token é realmente para reset de senha
        if (!"PASSWORD_RESET".equals(claims.get("type"))) {
            throw new InvalidTokenException("Token não autorizado para redefinição de senha.");
        }

        String username = claims.getSubject(); // subject = username
        User user = validateAndGetUserByUsername(username);

        // Atualiza a senha criptografada
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void sendPasswordForgotEmail(String email) {
        Optional<User> userOptional = getUserByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("User with email %s not found", email));
        }

        User user = userOptional.get();

        String token = tokenProvider.generatePasswordResetToken(user);

        PasswordForgotEvent event = new PasswordForgotEvent(
                user.getEmail(),
                token,
                user.getName());

        passwordForgotPublisher.publish(event);
    }
}