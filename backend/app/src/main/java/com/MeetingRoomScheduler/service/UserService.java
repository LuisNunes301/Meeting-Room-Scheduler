package com.MeetingRoomScheduler.service;

import java.util.List;
import java.util.Optional;

import com.MeetingRoomScheduler.domain.user.User;

public interface UserService {

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    User saveUser(User user);

    void deleteUser(User user);
}