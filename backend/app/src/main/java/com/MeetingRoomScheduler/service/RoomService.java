package com.MeetingRoomScheduler.service;

import com.MeetingRoomScheduler.domain.room.Room;
import java.util.List;

public interface RoomService {
    Room createRoom(Room room);

    Room getRoomById(Long id);

    List<Room> getAllRooms();

    List<Room> getAvailableRooms();

    List<Room> getRoomsByCapacity(Integer minCapacity);

    List<Room> getRoomsByLocation(String location);

    Room updateRoom(Room room);

    void deleteRoom(Long id);

    boolean roomExistsByName(String name);
}
