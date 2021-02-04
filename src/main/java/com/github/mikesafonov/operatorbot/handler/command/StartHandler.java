package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartHandler implements MessageHandler {
    private static final String END_LINE = "\n";

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageStart(chatId);
    }

    private SendMessage getMessageStart(long chatId) {
        StringBuilder text = new StringBuilder();
        text.append("Привет. Я умею назначать дежурных.").append(END_LINE);
        text.append("Для работы со мной воспользуйтесь командами.").append(END_LINE);
        text.append("[/help](/help) - Помощь.");
        return SendMessage.builder()
                .chatId(Long.toString(chatId))
                .parseMode(ParseMode.MARKDOWN)
                .text(text.toString())
                .build();
    }
}
