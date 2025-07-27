package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;
import com.MeetingRoomScheduler.execptions.ReservationNotFoundException;
import com.MeetingRoomScheduler.repository.ReservationRepository;
import com.MeetingRoomScheduler.service.ReservationService;
import com.MeetingRoomScheduler.service.RoomService;
import com.MeetingRoomScheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomService roomService;
    private final UserService userService;

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
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
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
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByRoomAndStatus(Long roomId, ReservationStatus status) {
        return reservationRepository.findByRoomIdAndStatus(roomId, status);
    }
}
