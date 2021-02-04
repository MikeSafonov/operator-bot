package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.model.*;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class WhoHandlerTest {
    @Mock
    private TimetableService timetableService;

    private WhoHandler whoHandler;

    private final User user = new User();
    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.WHO, "/who");
    private final Timetable timetable = new Timetable();
    private final User duty = new User();
    private final String text = "Дежурный сегодня: ";
    private final String textWhenNoDuty = "Что-то пошло не так! Дежурный на сегодня не назначен!";
    private final String textWhenNoAccess = "Команда не доступна!";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        whoHandler = new WhoHandler(timetableService);

        user.setFullName("User");
        user.setTelegramId(chatId);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.DUTY);
        user.setChatStatus(ChatStatus.NONE);
    }

    @Test
    public void shouldReturnWhoMessageWithAdminWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin(user);
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(text + timetable.getUserId().getFullName())
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithAdminWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal(user);

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoDuty)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithInternalUserWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal(user);
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(text + timetable.getUserId().getFullName())
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithInternalUserWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal(user);

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoDuty)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithExternalUserWhenDutyExists() throws TodayUserNotFoundException {
        user.setRole(Role.USER);
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal(user);
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoAccess)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithExternalUserWhenDutyNotExists() throws TodayUserNotFoundException {
        user.setRole(Role.USER);
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal(user);

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoAccess)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithUnknownUserWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoAccess)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithUnknownUserWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(textWhenNoAccess)
                .build();
        Assertions.assertEquals(expected, actual);
    }
}
