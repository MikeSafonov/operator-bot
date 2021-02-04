package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@RequiredArgsConstructor
public class DutyMessageHandler implements MessageHandler {
    private final TimetableService timetableService;
    private final UserService userService;

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageForDuty(chatId, user, parsedCommand.getText());
    }

    private SendMessage getMessageForDuty(long chatId, AuthorizationTelegram user, String message) {
        SendMessage operationResult;
        try {
            Timetable timetable = timetableService.findByTodayDate();
            long id = timetable.getUserId().getTelegramId();
            operationResult = new SendMessage().setChatId(id).setText(user.getUserFullName() + "\n\n" + message);
            userService.updateUserChatStatus(chatId, ChatStatus.NONE);
        } catch (TodayUserNotFoundException e) {
            log.error("Duty not found!", e);
            operationResult = new SendMessage().setChatId(chatId).setText("Что-то пошло не так! Дежурный не на месте!");
        }
        return operationResult;
    }
}
