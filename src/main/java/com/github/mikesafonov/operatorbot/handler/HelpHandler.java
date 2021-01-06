package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpHandler implements CommandHandler {
    private final String END_LINE = "\n";

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageHelp(chatId);
    }

    private SendMessage getMessageHelp(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("*Список основных сообщений*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - Показать стартовое сообщение.").append(END_LINE);
        text.append("[/help](/help) - Помощь.").append(END_LINE);
        text.append("[/role](/role) - Узнать свою роль.").append(END_LINE);
        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
