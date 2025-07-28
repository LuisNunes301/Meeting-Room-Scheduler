package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;
import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.execptions.ReservationNotFoundException;
import com.MeetingRoomScheduler.rabbit.ReservationCreatedPublisher;
import com.MeetingRoomScheduler.repository.ReservationRepository;
import com.MeetingRoomScheduler.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationCreatedPublisher reservationCreatedPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByRoomAndStatus(Long roomId, ReservationStatus status) {
        return reservationRepository.findByRoomIdAndStatus(roomId, status);
    }

    @Override
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        Reservation saved = reservationRepository.save(reservation);
        ReservationCreatedEvent event = toReservationCreatedEvent(saved);

        reservationCreatedPublisher.publish(event);

        return saved;
    }

    @Override
    @Transactional
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation validateAndGetReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + id + " not found"));
    }

    public ReservationCreatedEvent toReservationCreatedEvent(Reservation reservation) {
        return new ReservationCreatedEvent(
                reservation.getUser().getName(),
                reservation.getUser().getEmail(),
                reservation.getRoom().getName(),
                reservation.getRoom().getLocation(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                "admin@sistema.com");
    }

}