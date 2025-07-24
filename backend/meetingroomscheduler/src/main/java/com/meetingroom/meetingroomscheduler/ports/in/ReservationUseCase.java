package com.meetingroom.meetingroomscheduler.ports.in;

import java.util.List;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;

public interface ReservationUseCase {
    Reservation createReservation(Reservation reservation);

    Reservation getReservationById(UUID reservationId);

    List<Reservation> getReservationsByUser(UUID userId);
}