package com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.mapper;

import com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.RoomDTO;
import com.meetingroom.meetingroomscheduler.domain.model.Room;

public class RoomMapper {

    public static Room toDomain(RoomDTO dto) {
        return new Room(
                dto.getId(),
                dto.getName(),
                dto.getLocation(),
                dto.getCapacity());
    }

    public static RoomDTO toDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setLocation(room.getLocation());
        dto.setCapacity(room.getCapacity());
        return dto;
    }
}