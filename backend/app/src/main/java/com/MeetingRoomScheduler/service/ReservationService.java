package com.MeetingRoomScheduler.service;

import java.util.List;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;

public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByRoomId(Long roomId);

    List<Reservation> getReservationsByStatus(ReservationStatus status);

    List<Reservation> getReservationsByRoomAndStatus(Long roomId, ReservationStatus status);

    Reservation saveReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    public Reservation validateAndGetReservation(Long id);
}
