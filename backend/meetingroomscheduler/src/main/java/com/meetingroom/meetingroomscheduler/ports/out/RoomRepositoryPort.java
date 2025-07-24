package com.meetingroom.meetingroomscheduler.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Room;

public interface RoomRepositoryPort {
    Room save(Room room);

    Optional<Room> findById(UUID roomId);

    List<Room> findAll();
}