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
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageForDuty(chatId, user, parsedCommand.getText());
    }

    private SendMessage getMessageForDuty(String chatId, AuthorizationTelegram user, String message) {
        try {
            Timetable timetable = timetableService.findByTodayDate();
            String id = timetable.getUserId().getTelegramId();
            userService.updateUserChatStatus(chatId, ChatStatus.NONE);
            return SendMessage.builder()
                    .chatId(id)
                    .text(user.getUserFullName() + "\n\n" + message)
                    .build();

        } catch (TodayUserNotFoundException e) {
            log.error("Duty not found!", e);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Что-то пошло не так! Дежурный не на месте!")
                    .build();
        }
    }
}
