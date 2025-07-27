package com.MeetingRoomScheduler.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

        List<Reservation> findByRoomId(Long roomId);

        List<Reservation> findByUserId(Long userId);

        List<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);

        List<Reservation> findByStatus(ReservationStatus status);
}