package com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class RoomDTO {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    private int capacity;

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}