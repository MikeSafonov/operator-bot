package com.github.mikesafonov.operatorbot.handler.command.admin;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.ParserException;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.stream.Collectors;

/**
 * @author Mike Safonov
 */
@RequiredArgsConstructor
public class TimetableHandler implements MessageHandler {
    private static final int DEFAULT_COUNT = 14;
    private final TimetableService timetableService;
    private final Parser parser;

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        if (user.isAdmin()) {
            int count = getCountFromMessage(parsedCommand.getText());
            var timetable = timetableService.findTimetableInFuture(count);
            if (timetable.isEmpty()) {
                return SendMessage.builder()
                        .chatId(Long.toString(chatId))
                        .text("Нет назначенных дежурств")
                        .build();
            }
            var message = timetable.getContent().stream()
                    .map(t -> t.getTime().toString() + " - " + t.getUserId().getFullName())
                    .collect(Collectors.joining("\n"));
            return SendMessage.builder()
                    .chatId(Long.toString(chatId))
                    .text(message)
                    .build();

        } else {
            return SendMessage.builder()
                    .chatId(Long.toString(chatId))
                    .text("Команда не доступна!")
                    .build();
        }
    }

    private Integer getCountFromMessage(String message) {
        try {
            return Integer.parseInt(parser.getParamValue(message, 0, 1));
        } catch (NumberFormatException | ParserException e) {
            return DEFAULT_COUNT;
        }
    }
}
