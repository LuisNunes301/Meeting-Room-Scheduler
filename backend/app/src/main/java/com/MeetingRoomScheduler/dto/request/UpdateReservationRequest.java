package com.MeetingRoomScheduler.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

public record UpdateReservationRequest(
                @NotNull OffsetDateTime startTime,
                @NotNull OffsetDateTime endTime,
                Long roomId, // opcional: se o usuário/admin quiser mudar a sala
                ReservationStatus status // opcional: para admin atualizar o status
) {
}