package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.adapter;

import org.springframework.stereotype.Component;

import com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository.SpringReservationRepository;
import com.meetingroom.meetingroomscheduler.domain.model.Reservation;
import com.meetingroom.meetingroomscheduler.ports.out.ReservationRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReservationRepositoryAdapter implements ReservationRepositoryPort {

    private final SpringReservationRepository reservationRepository;

    public ReservationRepositoryAdapter(SpringReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(UUID reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public List<Reservation> findByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

}