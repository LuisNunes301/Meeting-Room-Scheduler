package com.MeetingRoomScheduler.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank @Size(min = 3, max = 50) String name,

        @NotBlank @Size(min = 3, max = 50) String username,

        @NotBlank @Email String email,

        String role // Pode ser null quando for o próprio usuário
) {
}
