package com.MeetingRoomScheduler.entities.room;

import java.time.LocalDateTime;

public record RoomDto(
        Long id,
        String name,
        String location,
        Integer capacity,
        Boolean available,
        String description,
        String equipment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt, Double price) {
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