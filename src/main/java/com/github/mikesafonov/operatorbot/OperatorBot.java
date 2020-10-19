package com.github.mikesafonov.operatorbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class OperatorBot extends TelegramLongPollingBot {
    
	@Value("${bot.name}")
	private String botUsername;
	@Value("${bot.token}")
	private String botToken;
	@Override
	public void onUpdateReceived(Update update) {
		if(update.getMessage() != null && update.getMessage().hasText()) {
			long chat_id = update.getMessage().getChatId();
			try {
				execute(new SendMessage(chat_id, update.getMessage().getText()));
			} catch(TelegramApiException e) {
				e.printStackTrace();
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
}
