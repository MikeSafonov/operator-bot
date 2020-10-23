package com.github.mikesafonov.operatorbot;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.service.InternalUserService;

@Component
public class OperatorBot extends TelegramLongPollingBot {

	private final InternalUserService internalUserService;

	public OperatorBot(InternalUserService internalUserService) {
		this.internalUserService = internalUserService;
	}

	@Value("${bot.name}")
	private String botUsername;
	@Value("${bot.token}")
	private String botToken;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			long chatId = update.getMessage().getChatId();

			Optional<InternalUser> optionalUser = internalUserService.findByTelegramId(chatId);

			optionalUser.ifPresentOrElse((value) -> {
				sendMessage(chatId, "Привет, " + optionalUser.get().getFullName() + ", давно не виделись!");
			}, () -> {
				sendMessage(chatId, "Я тебя не знаю, брысь!");
			});
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

	public void sendMessage(long chatId, String text) {
		try {
			execute(new SendMessage(chatId, text));
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}
}
