package com.MeetingRoomScheduler.dto.event;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreatedEvent {
    private String userName;
    private String userEmail;
    private String roomName;
    private String roomLocation;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String adminEmail;

}
