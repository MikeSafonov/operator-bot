package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RoleHandler implements MessageHandler {
    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageRole(chatId, user);
    }

    private SendMessage getMessageRole(String chatId, AuthorizationTelegram user) {
        var builder = SendMessage.builder()
                .chatId(chatId);

        if (user.isAdmin()) {
            return builder.text("Вы администратор.").build();
        } else if (user.isInternal()) {
            return builder.text("Вы внутренний пользователь.").build();
        } else if (user.isExternal()) {
            return builder.text("Вы внешний пользователь.").build();
        } else {
            return builder.text("Мы вас не знаем.").build();
        }
    }
}
