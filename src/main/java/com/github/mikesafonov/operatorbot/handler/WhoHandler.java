package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WhoHandler implements CommandHandler {
    private final TimetableService timetableService;

    private final static Logger logger = LoggerFactory.getLogger(WhoHandler.class.getName());

    public WhoHandler(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getWhoMessage(chatId, user);
    }

    private SendMessage getWhoMessage(long chatId, AuthorizationTelegram user) {
        SendMessage sendMessage = new SendMessage();
        if(user.isInternal()) {
            sendMessage = sendTodayDuty(chatId);
        } else {
            sendMessage.setChatId(chatId).setText("Команда не доступна!");
        }
        return sendMessage;
    }

    private SendMessage sendTodayDuty(long chatId) {
        SendMessage sendMessage = new SendMessage();
        try {
            Timetable timetable = timetableService.findByTodayDate();
            sendMessage.setChatId(chatId).setText("Дежурный сегодня: " + timetable.getUserId().getFullName());
        } catch (TodayUserNotFoundException e) {
            sendMessage.setChatId(chatId).setText("Что-то пошло не так! Дежурный на сегодня не назначен!");
            logger.error("We have no duty users today!", e);
        }
        return sendMessage;
    }
}
