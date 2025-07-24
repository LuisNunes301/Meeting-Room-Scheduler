package com.meetingroom.meetingroomscheduler.application.usecases;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;

public interface CreateReservationUseCase {
    Reservation createReservation(Reservation reservation);
}
