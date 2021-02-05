package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhenMeHandler;
import com.github.mikesafonov.operatorbot.model.Status;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WhenMeHandlerTest {
    @Mock
    private UserService userService;
    @Mock
    private TimetableService timetableService;
    @Mock
    private Parser parser;
    @Mock
    private AuthorizationTelegram authorization;

    private WhenMeHandler handler;

    private final String message = "/when_my_duty 1";
    private final ParsedCommand parsedCommand = new ParsedCommand();
    private final User user = new User();
    private final List<Timetable> timetableList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new WhenMeHandler(timetableService, userService, parser);
        user.setTelegramId("1");
        user.setFullName("N U P");
        user.setStatus(Status.ACTIVE);
    }

    @Test
    public void shouldReturnMessageWhenUserHaveDutiesWithParam() {
        parsedCommand.setCommand(Command.WHEN_MY_DUTY);
        parsedCommand.setText(message);
        LocalDate date = LocalDate.now();

        Timetable first = new Timetable();
        first.setTime(date);
        timetableList.add(first);
        Timetable second = new Timetable();
        second.setTime(date.plusDays(1));
        timetableList.add(second);
        Timetable third = new Timetable();
        third.setTime(date.plusDays(2));
        timetableList.add(third);
        Timetable fourth = new Timetable();
        fourth.setTime(date.plusDays(3));
        timetableList.add(fourth);
        Timetable fifth = new Timetable();
        fifth.setTime(date.plusDays(4));
        timetableList.add(fifth);
        Page page = new PageImpl(timetableList);

        Mockito.when(authorization.isInternal()).thenReturn(true);
        Mockito.when(userService.findByTelegramId("1")).thenReturn(Optional.of(user));
        Mockito.when(timetableService.findUsersDutyInFuture(user, 2)).thenReturn(page);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 1)).thenReturn("2");

        StringBuilder text = new StringBuilder();
        text.append("Твои дежурства:").append("\n").append("\n");
        for (int i = 0; i < 2; i++) {
            text.append(timetableList.get(i).getTime().toString()).append("\n");
        }

        SendMessage actual = handler.operate(user.getTelegramId(), authorization, parsedCommand);
        SendMessage expected =
                SendMessage.builder()
                        .chatId(user.getTelegramId())
                        .text(text.toString())
                        .build();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenUserHaveNoDuties() {
        parsedCommand.setCommand(Command.WHEN_MY_DUTY);
        parsedCommand.setText(message);
        Page page = new PageImpl(timetableList);

        Mockito.when(authorization.isInternal()).thenReturn(true);
        Mockito.when(userService.findByTelegramId("1")).thenReturn(Optional.of(user));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 1)).thenReturn("2");
        Mockito.when(timetableService.findUsersDutyInFuture(user, 2)).thenReturn(page);

        String text = "Ближайшие дежурства не назначены!";

        SendMessage actual = handler.operate(user.getTelegramId(), authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(text)
                .build();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenUserNotInternal() {
        parsedCommand.setCommand(Command.WHEN_MY_DUTY);
        parsedCommand.setText(message);

        Mockito.when(authorization.isInternal()).thenReturn(false);

        String text = "Команда не доступна!";

        SendMessage actual = handler.operate(user.getTelegramId(), authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(user.getTelegramId())
                .text(text)
                .build();

        Assertions.assertEquals(expected, actual);
    }
}
