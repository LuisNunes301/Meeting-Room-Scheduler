package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;
import com.meetingroom.meetingroomscheduler.domain.model.User;

public interface SpringUserRepository extends JpaRepository<User, UUID> {
    List<Reservation> findByUserId(UUID userId);
}