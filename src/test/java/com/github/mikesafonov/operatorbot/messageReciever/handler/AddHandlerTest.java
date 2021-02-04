package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.handler.command.admin.AddHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class AddHandlerTest {
    @Mock
    private UserService userService;
    @Mock
    private Parser parser;
    @Mock
    private AuthorizationTelegram authorization;

    private AddHandler addHandler;

    private final String message = "/add 1 N U P";
    private final String fullName = "N U P";
    private final long chatId = 0;
    private final long newUserId = 1;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.ADD_USER, message);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addHandler = new AddHandler(userService, parser);
    }

    @Test
    public void shouldReturnMessageIfNewUserAdded() {
        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn(String.valueOf(newUserId));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(fullName);
        SendMessage actual = addHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text("Пользователь успешно добавлен!")
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageIfNewUserExists() throws UserAlreadyExistException {
        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn(String.valueOf(newUserId));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(fullName);
        Mockito.when(userService.addUser(newUserId, fullName)).thenThrow(new UserAlreadyExistException("User with this id already exists!"));
        SendMessage actual = addHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text("Пользователь с таким id уже существует!")
                .build();
        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void shouldReturnMessageIfIncorrectCommandFormat() {
        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue("/add", 0, 2)).thenReturn(null);
        Mockito.when(parser.getParamValue("/add", 1, 2)).thenReturn(null);
        SendMessage actual = addHandler.operate(chatId, authorization, new ParsedCommand(Command.ADD_DUTY, "/add"));
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text("Команда введена неверно!")
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnMessageIfUserNotAdmin() {
        Mockito.when(authorization.isAdmin()).thenReturn(false);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn(String.valueOf(newUserId));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(fullName);
        SendMessage actual = addHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text("Команда не доступна!")
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldAddNewUser() throws UserAlreadyExistException {
        Mockito.when(authorization.isAdmin()).thenReturn(true);
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 0, 2)).thenReturn(String.valueOf(newUserId));
        Mockito.when(parser.getParamValue(parsedCommand.getText(), 1, 2)).thenReturn(fullName);
        addHandler.operate(chatId, authorization, parsedCommand);
        Mockito.verify(userService, Mockito.times(1)).addUser(newUserId, fullName);
    }
}
