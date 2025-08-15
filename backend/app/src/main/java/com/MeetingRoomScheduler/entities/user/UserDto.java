package com.MeetingRoomScheduler.entities.user;

import java.time.LocalDateTime;

public record UserDto(Long id, String username, String name, String email, String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt, boolean isActive) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getIsActive());
    }
}