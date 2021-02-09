package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.StartHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUser;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartHandlerTest {
    private final StartHandler startHandler = new StartHandler();

    private final User user = new User();
    private final StringBuilder text = new StringBuilder();
    private final String chatId = "0";
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.START, "/start");

    @BeforeEach
    public void setUp() {
        String END_LINE = "\n";
        text.append("Привет. Я умею назначать дежурных.").append(END_LINE);
        text.append("Для работы со мной воспользуйтесь командами.").append(END_LINE);
        text.append("[/help](/help) - Помощь.");

        user.setFullName("User");
        user.setTelegramId(chatId);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.DUTY);
    }

    @Test
    public void shouldReturnStartMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin(user);

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected =  SendMessage.builder()
                .chatId(chatId)
                .text(text.toString())
                .build();
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartMessageWithUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUser(user);

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected =  SendMessage.builder()
                .chatId(chatId)
                .text(text.toString())
                .build();
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartMessageWithUnknownUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected =  SendMessage.builder()
                .chatId(chatId)
                .text(text.toString())
                .build();
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }
}
