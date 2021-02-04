package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.StartChatHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

public class StartChatHandlerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthorizationTelegram authorization;

    private StartChatHandler startChatHandler;

    private ParsedCommand parsedCommand = new ParsedCommand(Command.START_CHAT, "/start_chat");
    long chatId = 0;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        startChatHandler = new StartChatHandler(userService);
    }

    @Test
    public void shouldReturnStartChatMessageWhenUserKnown() {
        Mockito.when(authorization.isUnknown()).thenReturn(false);
        SendMessage actual = startChatHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Введите сообщение дежурному.");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartChatMessageWhenUserUnknown() {
        Mockito.when(authorization.isUnknown()).thenReturn(true);
        SendMessage actual = startChatHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText("Команда не доступна!");
        Assertions.assertEquals(expected, actual);
    }
}
