package com.MeetingRoomScheduler.entities.Reservation;

import java.time.OffsetDateTime;

import com.MeetingRoomScheduler.entities.room.RoomDto;
import com.MeetingRoomScheduler.entities.user.UserDto;

public record ReservationDto(Long id, RoomDto room, UserDto user,
        OffsetDateTime createdAt,
        OffsetDateTime startTime,
        OffsetDateTime endTime, ReservationStatus status) {

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