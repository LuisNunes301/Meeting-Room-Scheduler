package com.MeetingRoomScheduler.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordForgotEvent {
    private String email;
    private String token;
    private String userName;
}