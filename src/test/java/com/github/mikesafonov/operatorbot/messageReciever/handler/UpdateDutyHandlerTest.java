package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.admin.UpdateDutyHandler;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class UpdateDutyHandlerTest {
    @Mock
    private InternalUserService internalUserService;
    @Mock
    private Parser parser;
    @Mock
    private TimetableService timetableService;
    @Mock
    private AuthorizationTelegram user;

    private final ParsedCommand parsedCommand = new ParsedCommand(Command.UPDATE_DUTY, "/update_duty 2020-12-12 111");
    private final long chatId = 0;

    private UpdateDutyHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new UpdateDutyHandler(timetableService, internalUserService, parser);
    }

    @Test
    public void shouldReturnMessageWhenDutyUpdated() {
        LocalDate date = LocalDate.of(2020, 12, 12);
        InternalUser duty = new InternalUser();
        duty.setTelegramId(111);

        Mockito.when(user.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(new Timetable()));
        Mockito.when(internalUserService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Дежурный успешно обновлен!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenDutyAdded() {
        InternalUser duty = new InternalUser();
        duty.setTelegramId(111);

        Mockito.when(user.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(internalUserService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Дежурный успешно обновлен!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenDutyNotExists() {
        LocalDate date = LocalDate.of(2020, 12, 12);
        InternalUser duty = new InternalUser();
        duty.setTelegramId(111);

        Mockito.when(user.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(new Timetable()));

        SendMessage actual = handler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Пользователя с таким id не существует!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenCommandIncorrectCommandFormat() {
        CharSequence charSequence = "202-14-0";
        InternalUser duty = new InternalUser();
        duty.setTelegramId(111);

        Mockito.when(user.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenThrow(new DateTimeParseException("", charSequence, 1));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(null);
        Mockito.when(internalUserService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Команда введена неверно!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenUserNotAdmin() {
        Mockito.when(user.isAdmin()).thenReturn(false);
        SendMessage actual = handler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Команда не доступна!");
        Assertions.assertEquals(expected, actual);
    }
}
