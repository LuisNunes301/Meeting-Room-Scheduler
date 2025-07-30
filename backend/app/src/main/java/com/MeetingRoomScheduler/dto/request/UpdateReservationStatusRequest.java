package com.MeetingRoomScheduler.dto.request;

import com.MeetingRoomScheduler.domain.Reservation.ReservationStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateReservationStatusRequest(
        @NotNull ReservationStatus status) {
}
