package com.github.mikesafonov.operatorbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;

@Component
public class OperatorBot extends TelegramLongPollingBot {
	private final AuthorizationService userAuthorization;
	private final TimetableService timetableService;

	public OperatorBot(AuthorizationService userAuthorization, TimetableService timetableService) {
		super();
		this.userAuthorization = userAuthorization;
		this.timetableService = timetableService;
	}

	@Value("${bot.name}")
	private String botUsername;
	@Value("${bot.token}")
	private String botToken;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			long chatId = update.getMessage().getChatId();
			String name = update.getMessage().getFrom().getUserName();
			AuthorizationTelegram user = userAuthorization.getInfo(chatId);

			if (user.isInternal()) {
				if (user.isAdmin()) {
					sendMessage(chatId, "Привет, " + name + ", теперь ты администратор!");
					try {
						Timetable timetable = timetableService.findByTodayDate();
						sendMessage(chatId, "Дежурный сегодня: " + timetable.getUserId().getFullName());
					} catch (TodayUserNotFoundException e) {
						sendMessage(chatId, "Что-то пошло не так! Дежурный на сегодня не назначен!");
					}
				} else {
					sendMessage(chatId, "Привет, " + name + ", давно не виделись!");
				}
			} else if (user.isExternal()) {
				sendMessage(chatId, "Привет, " + name + "!");
			} else if (user.isUnknown()) {
				sendMessage(chatId, "Привет, " + name + ", я тебя не знаю!");
			}
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