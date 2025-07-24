package com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.mapper;

import com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.ReservationDTO;
import com.meetingroom.meetingroomscheduler.domain.model.Reservation;

public class ReservationMapper {

    public static Reservation toDomain(ReservationDTO dto) {
        return new Reservation(
                dto.getId(),
                dto.getUserId(),
                dto.getRoomId(),
                dto.getStartTime(),
                dto.getEndTime());
    }

    public static ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUserId());
        dto.setRoomId(reservation.getRoomId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        return dto;
    }
}