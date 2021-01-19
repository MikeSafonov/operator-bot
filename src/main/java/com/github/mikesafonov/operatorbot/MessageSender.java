package com.github.mikesafonov.operatorbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class MessageSender extends DefaultAbsSender {
	@Value("${bot.token}")
	private String botToken;

	public MessageSender(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

}
