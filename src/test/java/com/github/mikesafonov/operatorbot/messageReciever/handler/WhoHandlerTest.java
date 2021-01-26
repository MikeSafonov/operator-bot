package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
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

    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.WHO, "/who");
    private final Timetable timetable = new Timetable();
    private final InternalUser duty = new InternalUser();
    private final String text = "Дежурный сегодня: ";
    private final String textWhenNoDuty = "Что-то пошло не так! Дежурный на сегодня не назначен!";
    private final String textWhenNoAccess = "Команда не доступна!";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        whoHandler = new WhoHandler(timetableService);
    }

    @Test
    public void shouldReturnWhoMessageWithAdminWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramAdmin();
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text + timetable.getUserId().getFullName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithAdminWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramInternal();

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoDuty);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithInternalUserWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramInternal();
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text + timetable.getUserId().getFullName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithInternalUserWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramInternal();

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoDuty);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithExternalUserWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramExternal();
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoAccess);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithExternalUserWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramExternal();

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoAccess);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithUnknownUserWhenDutyExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramUnknown();
        timetable.setUserId(duty);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoAccess);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoMessageWithUnknownUserWhenDutyNotExists() throws TodayUserNotFoundException {
        AuthorizationTelegram user = new AuthorizationTelegramUnknown();

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We have no duty users today!"));

        SendMessage actual = whoHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(textWhenNoAccess);
        Assertions.assertEquals(expected, actual);
    }
}
