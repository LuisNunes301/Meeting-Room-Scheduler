package com.meetingroom.meetingroomscheduler.application.service;

import java.util.List;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;
import com.meetingroom.meetingroomscheduler.ports.in.ReservationUseCase;
import com.meetingroom.meetingroomscheduler.ports.out.ReservationRepositoryPort;

public class ReservationService implements ReservationUseCase {

    private final ReservationRepositoryPort reservationRepository;

    public ReservationService(ReservationRepositoryPort reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    @Override
    public List<Reservation> getReservationsByUser(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }
}