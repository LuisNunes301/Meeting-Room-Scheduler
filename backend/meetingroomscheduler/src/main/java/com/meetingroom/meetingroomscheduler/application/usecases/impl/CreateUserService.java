package com.meetingroom.meetingroomscheduler.application.usecases.impl;

import org.springframework.stereotype.Service;

import com.meetingroom.meetingroomscheduler.application.usecases.CreateUserUseCase;
import com.meetingroom.meetingroomscheduler.domain.model.User;
import com.meetingroom.meetingroomscheduler.ports.out.UserRepositoryPort;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public CreateUserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(User user) {
        return userRepositoryPort.save(user);
    }
}
