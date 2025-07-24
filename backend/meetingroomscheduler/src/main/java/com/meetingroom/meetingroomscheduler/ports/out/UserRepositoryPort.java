package com.meetingroom.meetingroomscheduler.ports.out;

import java.util.Optional;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(UUID userId);
}