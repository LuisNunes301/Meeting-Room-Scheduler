package com.meetingroom.meetingroomscheduler.domain.model;

import java.util.UUID;

public class Room {
    private UUID id;
    private String name;
    private String location;
    private int capacity;
    private boolean available;

    public Room(UUID id, String name, String location, int capacity, boolean available) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
    }

    public Room() {

    }

    public Room(UUID id, String name, String location, int capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailability(boolean available) {
        this.available = available;
    }
}