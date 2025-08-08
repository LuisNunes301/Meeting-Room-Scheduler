package com.MeetingRoomScheduler.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(@Schema(example = "user3") @NotBlank String username,
                @Schema(example = "user3") @NotBlank String password,
                @Schema(example = "User3") @NotBlank String name,
                @Schema(example = "user3@mycompany.com") @Email String email) {

}
