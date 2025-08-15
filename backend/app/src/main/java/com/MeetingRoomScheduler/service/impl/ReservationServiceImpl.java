package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.dto.event.ReservationStatusUpdatedEvent;
import com.MeetingRoomScheduler.dto.request.UpdateReservationRequest;
import com.MeetingRoomScheduler.entities.Reservation.Reservation;
import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;
import com.MeetingRoomScheduler.entities.room.Room;
import com.MeetingRoomScheduler.execptions.ReservationNotFoundException;
import com.MeetingRoomScheduler.rabbit.reservation.publisher.ReservationCreatedPublisher;
import com.MeetingRoomScheduler.rabbit.reservation.publisher.ReservationStatusUpdatedPublisher;
import com.MeetingRoomScheduler.repository.ReservationRepository;
import com.MeetingRoomScheduler.service.ReservationService;
import com.MeetingRoomScheduler.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final ReservationCreatedPublisher reservationCreatedPublisher;
    private final ReservationStatusUpdatedPublisher reservationStatusUpdatedPublisher;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByFilters(Long roomId, ReservationStatus status) {
        return reservationRepository.findAll().stream()
                .filter(r -> roomId == null || r.getRoom().getId().equals(roomId))
                .filter(r -> status == null || r.getStatus().equals(status))
                .toList();
    }

    @Override
    @Transactional
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
        reservationCreatedPublisher.publish(toReservationCreatedEvent(saved));

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
    public Reservation cancelReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation confirmReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation releaseReservationSlot(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservationStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = validateAndGetReservation(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled reservations cannot be changed.");
        }

        ReservationStatus oldStatus = reservation.getStatus();

        if (newStatus == ReservationStatus.CANCELLED) {
            cancelReservation(reservation);
        } else if (reservation.getStatus() == ReservationStatus.PENDING && newStatus == ReservationStatus.CONFIRMED) {
            confirmReservation(reservation);
        } else {
            throw new IllegalStateException("Invalid status transition from " + oldStatus + " to " + newStatus);
        }

        Reservation updated = reservationRepository.save(reservation);

        reservationStatusUpdatedPublisher.publish(new ReservationStatusUpdatedEvent(
                updated.getId(),
                updated.getUser().getEmail(),
                updated.getRoom().getName(),
                oldStatus,
                newStatus,
                LocalDateTime.now()));

        return updated;
    }

    @Override
    public Reservation updateReservationAsAdmin(Long id, UpdateReservationRequest request) {
        Reservation reservation = validateAndGetReservation(id);
        applyChanges(reservation, request);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservationAsUser(Long id, UpdateReservationRequest request, String username) {
        Reservation reservation = validateAndGetReservation(id);

        if (!reservation.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You cannot update another user's reservation");
        }

        applyChanges(reservation, request);
        return reservationRepository.save(reservation);
    }

    @Override
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

    private void applyChanges(Reservation reservation, UpdateReservationRequest request) {
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());

        if (request.roomId() != null) {
            Room room = roomService.getRoomById(request.roomId());
            reservation.setRoom(room);
        }

        if (request.status() != null) {
            reservation.setStatus(request.status());
        }
    }
}