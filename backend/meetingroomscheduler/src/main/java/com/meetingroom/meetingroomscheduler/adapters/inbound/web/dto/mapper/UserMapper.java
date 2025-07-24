package com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.mapper;

import com.meetingroom.meetingroomscheduler.adapters.inbound.web.dto.UserDTO;
import com.meetingroom.meetingroomscheduler.domain.model.User;

public class UserMapper {

    public static User toDomain(UserDTO dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail());
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}