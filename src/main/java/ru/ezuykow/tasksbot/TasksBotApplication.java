package ru.ezuykow.tasksbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TasksBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksBotApplication.class, args);
	}

}
