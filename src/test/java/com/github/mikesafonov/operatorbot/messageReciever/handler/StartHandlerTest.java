package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.StartHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartHandlerTest {
    private final StartHandler startHandler = new StartHandler();

    private final StringBuilder text = new StringBuilder();
    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.START, "/start");

    @BeforeEach
    public void setUp() {
        String END_LINE = "\n";
        text.append("Привет. Я умею назначать дежурных.").append(END_LINE);
        text.append("Для работы со мной воспользуйтесь командами.").append(END_LINE);
        text.append("[/help](/help) - Помощь.");
    }

    @Test
    public void shouldReturnStartMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin();

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartMessageWithInternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal();

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartMessageWithExternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal();

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartMessageWithUnknownUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = startHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }
}
