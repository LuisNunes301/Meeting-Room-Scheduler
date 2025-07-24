package com.meetingroom.meetingroomscheduler.application.usecases;

import com.meetingroom.meetingroomscheduler.domain.model.Room;

public interface CreateRoomUseCase {
    Room createRoom(Room room);
}