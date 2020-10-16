package com.github.mikesafonov.operatorbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class OperatorBotApplication {

	public static void main(String[] args) {
		
		ApiContextInitializer.init();
		
		SpringApplication.run(OperatorBotApplication.class, args);
	}

}
