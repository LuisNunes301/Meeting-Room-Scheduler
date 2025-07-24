package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository.SpringUserRepository;
import com.meetingroom.meetingroomscheduler.domain.model.User;
import com.meetingroom.meetingroomscheduler.ports.out.UserRepositoryPort;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringUserRepository userRepository;

    public UserRepositoryAdapter(SpringUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

}
