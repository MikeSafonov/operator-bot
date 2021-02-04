package com.github.mikesafonov.operatorbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OperatorBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperatorBotApplication.class, args);
	}

}
