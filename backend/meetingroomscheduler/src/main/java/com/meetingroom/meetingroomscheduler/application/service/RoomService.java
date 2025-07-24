package com.meetingroom.meetingroomscheduler.application.service;

import java.util.List;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Room;
import com.meetingroom.meetingroomscheduler.ports.in.RoomUseCase;
import com.meetingroom.meetingroomscheduler.ports.out.RoomRepositoryPort;

public class RoomService implements RoomUseCase {

    private final RoomRepositoryPort roomRepository;

    public RoomService(RoomRepositoryPort roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}