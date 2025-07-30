package com.MeetingRoomScheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomRequest(
                @NotBlank(message = "Nome da sala é obrigatório") String name,

                @NotBlank(message = "Localização é obrigatória") String location,

                @NotNull(message = "Capacidade é obrigatória") @Positive(message = "Capacidade deve ser maior que zero") Integer capacity,

                @NotNull(message = "Disponibilidade é obrigatória") Boolean available,

                String description,
                String equipment,
                Double price) {
}