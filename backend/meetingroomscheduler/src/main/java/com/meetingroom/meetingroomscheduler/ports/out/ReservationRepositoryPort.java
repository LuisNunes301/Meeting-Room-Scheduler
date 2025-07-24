package com.meetingroom.meetingroomscheduler.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(UUID reservationId);

    List<Reservation> findByUserId(UUID userId);
}