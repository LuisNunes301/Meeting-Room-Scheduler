package com.MeetingRoomScheduler.entities.Reservation;

import java.time.LocalDateTime;

import com.MeetingRoomScheduler.entities.room.RoomDto;
import com.MeetingRoomScheduler.entities.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationDto(Long id, RoomDto room, UserDto user,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime startTime,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime endTime, ReservationStatus status) {

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