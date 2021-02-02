package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RoleHandler implements MessageHandler {
    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageRole(chatId, user);
    }

    private SendMessage getMessageRole(long chatId, AuthorizationTelegram user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (user.isAdmin()) {
            sendMessage.setText("Вы администратор.");
        } else if (user.isInternal()) {
            sendMessage.setText("Вы внутренний пользователь.");
        } else if (user.isExternal()) {
            sendMessage.setText("Вы внешний пользователь.");
        } else {
            sendMessage.setText("Мы вас не знаем.");
        }
        return sendMessage;
    }
}
