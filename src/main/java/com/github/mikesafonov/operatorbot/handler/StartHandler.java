package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartHandler implements CommandHandler {
    private final String END_LINE = "\n";

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageStart(chatId);
    }

    private SendMessage getMessageStart(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("Привет. Я умею назначать дежурных.").append(END_LINE);
        text.append("Для работы со мной воспользуйтесь командами.").append(END_LINE);
        text.append("[/help](/help) - Помощь.");
        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
