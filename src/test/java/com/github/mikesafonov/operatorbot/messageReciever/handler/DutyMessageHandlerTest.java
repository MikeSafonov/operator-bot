package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.DutyMessageHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
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

public class DutyMessageHandlerTest {
    @Mock
    private UserService userService;
    @Mock
    private TimetableService timetableService;
    @Mock
    private AuthorizationTelegram authorization;

    private final ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "I have an issue!");
    private final String chatId = "0";
    private final User duty = new User();
    private final User issuer = new User();

    private DutyMessageHandler dutyMessageHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dutyMessageHandler = new DutyMessageHandler(timetableService, userService);
    }

    @Test
    public void shouldReturnMessageForDuty() {
        String dutyId = "1";
        duty.setTelegramId(dutyId);
        Timetable timetable = new Timetable();
        timetable.setUserId(duty);
        issuer.setFullName("User");
        issuer.setTelegramId(chatId);
        issuer.setChatStatus(ChatStatus.NONE);

        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);
        Mockito.when(authorization.getUserFullName()).thenReturn(issuer.getFullName());

        SendMessage actual = dutyMessageHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(dutyId)
                .text(issuer.getFullName() + "\n" + "\n" + parsedCommand.getText())
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageWhenNoDuty() {
        issuer.setChatStatus(ChatStatus.NONE);

        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException(""));
        Mockito.when(authorization.getUserFullName()).thenReturn(issuer.getFullName());

        SendMessage actual = dutyMessageHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(chatId)
                .text("Что-то пошло не так! Дежурный не на месте!")
                .build();
        Assertions.assertEquals(expected, actual);
    }
}
