package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class DefaultHandler implements MessageHandler {
    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageDefault(chatId);
    }

    private SendMessage getMessageDefault(long chatId) {
        return new SendMessage().setChatId(chatId).setText("Выберите действие.");
    }
}
