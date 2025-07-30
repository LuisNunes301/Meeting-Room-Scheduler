package com.MeetingRoomScheduler.service;

import java.util.List;

import com.MeetingRoomScheduler.entities.room.Room;

public interface RoomService {
    Room createRoom(Room room);

    Room getRoomById(Long id);

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByCapacity(Integer minCapacity);

    List<Room> getRoomsByLocation(String location);

    Room validateAndGetRoom(Long id);

    Room updateRoom(Room room);

    void deleteRoom(Long id);

    boolean roomExistsByName(String name);
}
