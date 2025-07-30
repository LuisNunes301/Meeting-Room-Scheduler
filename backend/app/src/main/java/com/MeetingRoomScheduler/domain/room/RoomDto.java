package com.MeetingRoomScheduler.domain.room;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RoomDto(
        Long id,
        String name,
        String location,
        Integer capacity,
        Boolean available,
        String description,
        String equipment,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime updatedAt, Double price) {
    public static RoomDto from(Room room) {
        return new RoomDto(
                room.getId(),
                room.getName(),
                room.getLocation(),
                room.getCapacity(),
                room.getAvailable(),
                room.getDescription(),
                room.getEquipment(),
                room.getCreatedAt(),
                room.getUpdatedAt(), room.getPrice());
    }
}