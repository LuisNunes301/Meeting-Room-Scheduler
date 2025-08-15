package com.MeetingRoomScheduler.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record CreateReservationRequest(
        @NotNull Long roomId,
        @FutureOrPresent @NotNull OffsetDateTime startTime,
        @FutureOrPresent @NotNull OffsetDateTime endTime) {
}