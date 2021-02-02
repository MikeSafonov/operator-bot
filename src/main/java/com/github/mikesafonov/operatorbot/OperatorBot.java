package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.service.MessageReciever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class OperatorBot extends TelegramLongPollingBot {
	private final MessageReciever messageReciever;

	public OperatorBot(MessageReciever messageReciever, DefaultBotOptions options) {
		super(options);
		this.messageReciever = messageReciever;
	}

	@Value("${bot.name}")
	private String botUsername;
	@Value("${bot.token}")
	private String botToken;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			sendMessage(messageReciever.analyze(update));
		}
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	private void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException ex) {
			log.debug("Message didn't send. " + ex);
		}
	}
}
