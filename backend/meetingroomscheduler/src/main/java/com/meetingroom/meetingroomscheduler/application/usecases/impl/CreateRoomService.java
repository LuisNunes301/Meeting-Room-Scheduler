package com.meetingroom.meetingroomscheduler.application.usecases.impl;

import org.springframework.stereotype.Service;

import com.meetingroom.meetingroomscheduler.application.usecases.CreateRoomUseCase;
import com.meetingroom.meetingroomscheduler.domain.model.Room;
import com.meetingroom.meetingroomscheduler.ports.out.RoomRepositoryPort;

@Service
public class CreateRoomService implements CreateRoomUseCase {

    private final RoomRepositoryPort roomRepositoryPort;

    public CreateRoomService(RoomRepositoryPort roomRepositoryPort) {
        this.roomRepositoryPort = roomRepositoryPort;
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepositoryPort.save(room);
    }
}
