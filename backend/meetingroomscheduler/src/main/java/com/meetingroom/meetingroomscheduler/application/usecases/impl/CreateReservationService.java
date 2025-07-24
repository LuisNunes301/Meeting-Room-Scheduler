package com.meetingroom.meetingroomscheduler.application.usecases.impl;

import org.springframework.stereotype.Service;

import com.meetingroom.meetingroomscheduler.application.usecases.CreateReservationUseCase;
import com.meetingroom.meetingroomscheduler.domain.model.Reservation;
import com.meetingroom.meetingroomscheduler.ports.out.ReservationRepositoryPort;

@Service
public class CreateReservationService implements CreateReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;

    public CreateReservationService(ReservationRepositoryPort reservationRepositoryPort) {
        this.reservationRepositoryPort = reservationRepositoryPort;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepositoryPort.save(reservation);
    }
}