package com.meetingroom.meetingroomscheduler.ports.in;

import java.util.List;
import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.model.Room;

public interface RoomUseCase {
    Room createRoom(Room room);

    Room getRoomById(UUID roomId);

    List<Room> getAllRooms();
}