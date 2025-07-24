package com.meetingroom.meetingroomscheduler.application.usecases;

import com.meetingroom.meetingroomscheduler.domain.model.User;

public interface CreateUserUseCase {
    User createUser(User user);
}