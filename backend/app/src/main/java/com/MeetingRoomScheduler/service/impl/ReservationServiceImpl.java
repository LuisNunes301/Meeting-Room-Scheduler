package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.dto.event.ReservationStatusUpdatedEvent;
import com.MeetingRoomScheduler.entities.Reservation.Reservation;
import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;
import com.MeetingRoomScheduler.execptions.ReservationNotFoundException;
import com.MeetingRoomScheduler.rabbit.reservation.publisher.ReservationCreatedPublisher;
import com.MeetingRoomScheduler.rabbit.reservation.publisher.ReservationStatusUpdatedPublisher;
import com.MeetingRoomScheduler.repository.ReservationRepository;
import com.MeetingRoomScheduler.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationCreatedPublisher reservationCreatedPublisher;
    private final ReservationStatusUpdatedPublisher reservationStatusUpdatedPublisher;

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

    public Reservation saveReservation(Reservation reservation) {
        boolean hasConflict = reservationRepository.existsByRoomIdAndTimeOverlap(
                reservation.getRoom().getId(),
                reservation.getStartTime(),
                reservation.getEndTime());

        if (hasConflict) {
            throw new IllegalArgumentException(
                    "There is already a reservation for this room in the selected time interval.");
        }

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

    @Override
    @Transactional
    public void cancelReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void confirmReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void releaseReservationSlot(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);
        reservationRepository.save(reservation);
        System.out.println("Slot released: " + reservation.getRoom().getName()
                + " from " + reservation.getStartTime() + " to " + reservation.getEndTime());
    }

    @Override
    public Reservation updateReservationStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = validateAndGetReservation(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled reservations cannot be changed.");
        }

        ReservationStatus oldStatus = reservation.getStatus();

        // Handle status transitions
        if (newStatus == ReservationStatus.CANCELLED) {
            cancelReservation(reservation);
        } else if (reservation.getStatus() == ReservationStatus.PENDING && newStatus == ReservationStatus.CONFIRMED) {
            confirmReservation(reservation);
        } else {
            throw new IllegalStateException("Invalid status transition from " + oldStatus + " to " + newStatus);
        }

        Reservation updated = reservationRepository.save(reservation);

        ReservationStatusUpdatedEvent event = new ReservationStatusUpdatedEvent(
                updated.getId(),
                updated.getUser().getEmail(),
                updated.getRoom().getName(),
                oldStatus,
                newStatus,
                LocalDateTime.now());

        reservationStatusUpdatedPublisher.publish(event);

        return updated;
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