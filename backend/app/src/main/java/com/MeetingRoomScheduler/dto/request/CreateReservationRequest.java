package com.MeetingRoomScheduler.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotNull Long roomId,
        @Future @NotNull LocalDateTime startTime,
        @Future @NotNull LocalDateTime endTime) {
}