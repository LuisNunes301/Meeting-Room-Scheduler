package com.MeetingRoomScheduler.domain.Reservation;

import java.time.Instant;
import java.time.LocalDateTime;

import com.MeetingRoomScheduler.domain.room.RoomDto;
import com.MeetingRoomScheduler.domain.user.UserDto;

public record ReservationDto(Long id, RoomDto room, UserDto user, Instant createdAt, LocalDateTime startTime,
        LocalDateTime endTime, ReservationStatus status) {

    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                RoomDto.from(reservation.getRoom()),
                UserDto.from(reservation.getUser()), // Properly initialize UserDto
                reservation.getCreatedAt(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus());
    }
}