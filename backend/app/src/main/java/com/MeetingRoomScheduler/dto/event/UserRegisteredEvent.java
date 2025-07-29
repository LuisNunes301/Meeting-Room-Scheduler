package com.MeetingRoomScheduler.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Novo usuário cadastrado	EVENTUserRegisteredEvent	FILARABBITMQ user.registered	TEMPLATE welcome.html	to?Usuário
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private String userName;
    private String userEmail;
}
