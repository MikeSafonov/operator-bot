package com.github.mikesafonov.operatorbot.handler.command.internal;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
public class WhoHandler implements MessageHandler {
    private final TimetableService timetableService;

    public WhoHandler(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getWhoMessage(chatId, user);
    }

    private SendMessage getWhoMessage(String chatId, AuthorizationTelegram user) {
        if (user.isInternal()) {
            return sendTodayDuty(chatId);
        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Команда не доступна!")
                    .build();
        }
    }

    private SendMessage sendTodayDuty(String chatId) {
        try {
            Timetable timetable = timetableService.findByTodayDate();
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Дежурный сегодня: " + timetable.getUserId().getFullName())
                    .build();
        } catch (TodayUserNotFoundException e) {
            log.error("We have no duty users today!", e);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Что-то пошло не так! Дежурный на сегодня не назначен!")
                    .build();
        }
    }
}
