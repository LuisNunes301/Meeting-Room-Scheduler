package com.MeetingRoomScheduler.service;

import java.util.List;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.entities.Reservation.Reservation;
import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByRoomId(Long roomId);

    List<Reservation> getReservationsByUserId(Long userId);

    List<Reservation> getReservationsByStatus(ReservationStatus status);

    List<Reservation> getReservationsByRoomAndStatus(Long roomId, ReservationStatus status);

    Reservation saveReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    Reservation validateAndGetReservation(Long id);

    ReservationCreatedEvent toReservationCreatedEvent(Reservation reservation);

    Reservation updateReservationStatus(Long id, ReservationStatus newStatus);

    void releaseReservationSlot(Reservation reservation);

    void cancelReservation(Reservation reservation);

    void confirmReservation(Reservation reservation);
}
