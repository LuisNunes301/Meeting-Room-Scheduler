package com.MeetingRoomScheduler.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record RoomResponse(
        Long id,
        String name,
        String location,
        Integer capacity,
        Boolean available,
        String description,
        String equipment,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime updatedAt) {
}