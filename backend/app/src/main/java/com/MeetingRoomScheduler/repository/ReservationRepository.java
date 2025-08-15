package com.MeetingRoomScheduler.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.MeetingRoomScheduler.entities.Reservation.Reservation;
import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByRoomId(Long roomId);

  List<Reservation> findByUserId(Long userId);

  List<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);

  List<Reservation> findByStatus(ReservationStatus status);

  @Query("""
          SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
          FROM Reservation r
          WHERE r.room.id = :roomId
            AND r.status != 'CANCELLED'
            AND (
              (r.startTime < :endTime AND r.endTime > :startTime)
            )
      """)
  boolean existsByRoomIdAndTimeOverlap(
      @Param("roomId") Long roomId,
      @Param("startTime") OffsetDateTime startTime,
      @Param("endTime") OffsetDateTime endTime);
}