package com.MeetingRoomScheduler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PasswordResetRequest(
        @Schema(description = "Password reset token") String token,
        @Schema(description = "New password") String newPassword) {
}