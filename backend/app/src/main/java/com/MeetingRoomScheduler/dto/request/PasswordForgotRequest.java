package com.MeetingRoomScheduler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record PasswordForgotRequest(
                @Schema(example = "user3@mycompany.com") @Email String email) {
}
