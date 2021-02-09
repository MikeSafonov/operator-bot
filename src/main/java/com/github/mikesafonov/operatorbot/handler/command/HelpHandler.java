package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HelpHandler implements MessageHandler {
    private static final String END_LINE = "\n";

    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        var builder = SendMessage.builder()
                .chatId(chatId)
                .parseMode(ParseMode.MARKDOWN);

        if (user.isAdmin()) {
            return builder.text(buildAdminMessage()).build();
        }

        if (user.isUser()) {
            return builder.text(buildDutyMessage()).build();
        }

        return builder.text("Обратитесь к администратору").build();
    }

    private String buildAdminMessage() {
        return Arrays.stream(Command.values())
                .filter(Command::isAdmin)
                .map(Command::getDescription)
                .collect(Collectors.joining(END_LINE));
    }

    private String buildDutyMessage() {
        return Arrays.stream(Command.values())
                .filter(Command::isInternal)
                .map(Command::getDescription)
                .collect(Collectors.joining(END_LINE));
    }
}
