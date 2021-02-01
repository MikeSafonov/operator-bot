package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.admin.UpdateDutyHandler;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
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
    private UserService userService;
    @Mock
    private Parser parser;
    @Mock
    private TimetableService timetableService;
    @Mock
    private AuthorizationTelegram authorization;

    private final ParsedCommand parsedCommand = new ParsedCommand(Command.UPDATE_DUTY, "/update_duty 2020-12-12 111");
    private final long chatId = 0;

    private UpdateDutyHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new UpdateDutyHandler(timetableService, userService, parser);
    }

    @Test
    public void shouldReturnMessageWhenDutyUpdated() {
        LocalDate date = LocalDate.of(2020, 12, 12);
        User duty = new User();
        duty.setTelegramId(111);

        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(new Timetable()));
        Mockito.when(userService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Дежурный успешно обновлен!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenDutyAdded() {
        User duty = new User();
        duty.setTelegramId(111);

        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(userService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Дежурный успешно обновлен!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenDutyNotExists() {
        LocalDate date = LocalDate.of(2020, 12, 12);
        User duty = new User();
        duty.setTelegramId(111);

        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn("2020-12-12");
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn("111");
        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(new Timetable()));

        SendMessage actual = handler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Пользователя с таким id не существует!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenCommandIncorrectCommandFormat() {
        CharSequence charSequence = "202-14-0";
       User duty = new User();
        duty.setTelegramId(111);

        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenThrow(new DateTimeParseException("", charSequence, 1));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(null);
        Mockito.when(userService.findByTelegramId(111)).thenReturn(Optional.of(duty));

        SendMessage actual = handler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Команда введена неверно!");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenUserNotAdmin() {
        Mockito.when(authorization.isAdmin()).thenReturn(false);
        SendMessage actual = handler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Команда не доступна!");
        Assertions.assertEquals(expected, actual);
    }
}
