package com.MeetingRoomScheduler.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

public record UpdateReservationRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        Long roomId, // opcional: se o usuário/admin quiser mudar a sala
        ReservationStatus status // opcional: para admin atualizar o status
) {
}