package com.MeetingRoomScheduler.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UserChangePasswordRequest(
        @Schema(example = "user3@mycompany.com") @Email String email,
        @Schema(example = "newPassword123") String newPassword) {
}