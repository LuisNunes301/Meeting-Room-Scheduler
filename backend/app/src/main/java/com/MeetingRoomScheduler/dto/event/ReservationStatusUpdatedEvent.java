package com.MeetingRoomScheduler.dto.event;

import java.time.LocalDateTime;

import com.MeetingRoomScheduler.entities.Reservation.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusUpdatedEvent {
    private Long reservationId;
    private String userEmail;
    private String roomName;
    private ReservationStatus oldStatus;
    private ReservationStatus newStatus;
    private LocalDateTime updatedAt;
}
