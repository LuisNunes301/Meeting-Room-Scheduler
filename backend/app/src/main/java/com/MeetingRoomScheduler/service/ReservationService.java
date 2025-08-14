package com.MeetingRoomScheduler.service;

import java.util.List;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.dto.request.UpdateReservationRequest;
import com.MeetingRoomScheduler.entities.Reservation.Reservation;
import com.MeetingRoomScheduler.entities.Reservation.ReservationDto;
import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Reservation> getReservationsByUserId(Long userId);

    List<Reservation> getReservationsByFilters(Long roomId, ReservationStatus status);

    // Criação e persistência
    Reservation saveReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    // Validação
    Reservation validateAndGetReservation(Long id);

    // Atualizações de status
    Reservation cancelReservation(Reservation reservation);

    Reservation confirmReservation(Reservation reservation);

    Reservation releaseReservationSlot(Reservation reservation);

    Reservation updateReservationStatus(Long id, ReservationStatus newStatus);

    // Atualizações via request
    Reservation updateReservationAsAdmin(Long id, UpdateReservationRequest request);

    Reservation updateReservationAsUser(Long id, UpdateReservationRequest request, String username);

    // Eventos
    ReservationCreatedEvent toReservationCreatedEvent(Reservation reservation);
}
