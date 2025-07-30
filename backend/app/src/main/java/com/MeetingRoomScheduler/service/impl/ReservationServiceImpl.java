package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;
import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.execptions.ReservationNotFoundException;
import com.MeetingRoomScheduler.rabbit.reservation.ReservationCreatedPublisher;
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

    @Override
    public void releaseReservationSlot(Reservation reservation) {
        // Você pode personalizar isso conforme sua lógica.
        System.out.println("Slot liberado: " + reservation.getRoom().getName()
                + " de " + reservation.getStartTime() + " até " + reservation.getEndTime());
    }

    @Override
    public Reservation updateReservationStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = validateAndGetReservation(id);

        // Impede alterações em reservas canceladas
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservas canceladas não podem ser alteradas.");
        }

        // Validação de transições
        if (reservation.getStatus() == ReservationStatus.PENDING && newStatus == ReservationStatus.CONFIRMED) {
            // OK
        } else if (newStatus == ReservationStatus.CANCELLED) {
            // OK
        } else {
            throw new IllegalStateException("Transição de status inválida.");
        }

        // Atualiza status
        reservation.setStatus(newStatus);

        // Libera slot se for cancelado
        if (newStatus == ReservationStatus.CANCELLED) {
            releaseReservationSlot(reservation);
        }

        return reservationRepository.save(reservation);
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