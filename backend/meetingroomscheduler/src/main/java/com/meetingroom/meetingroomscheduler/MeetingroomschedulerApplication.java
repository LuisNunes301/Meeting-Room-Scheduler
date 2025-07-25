package com.meetingroom.meetingroomscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({
		"com.meetingroom.meetingroomscheduler.adapters.outbound.entity",
		"com.meetingroom.meetingroomscheduler.domain.model"
})
@EnableJpaRepositories("com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.repository")
@ComponentScan(basePackages = "com.meetingroom.meetingroomscheduler")
public class MeetingroomschedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingroomschedulerApplication.class, args);
	}

}
