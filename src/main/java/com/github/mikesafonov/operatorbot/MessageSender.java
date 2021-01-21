package com.github.mikesafonov.operatorbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
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

	public void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException ex) {
			log.error("Message didn't send. " + ex);
		}
	}
}
