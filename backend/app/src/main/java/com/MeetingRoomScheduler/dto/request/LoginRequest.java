package com.MeetingRoomScheduler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @Schema(example = "user") @NotBlank String username,
                @Schema(example = "user") @NotBlank String password) {
}
