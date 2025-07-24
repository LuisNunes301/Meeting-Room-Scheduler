package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;

public interface SpringReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUserId(UUID userId);
}