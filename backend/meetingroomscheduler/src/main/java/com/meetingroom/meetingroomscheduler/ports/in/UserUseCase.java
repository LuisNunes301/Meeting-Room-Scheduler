package com.meetingroom.meetingroomscheduler.ports.in;

import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.User;

public interface UserUseCase {
    User createUser(User user);

    User getUserById(UUID userId);
}