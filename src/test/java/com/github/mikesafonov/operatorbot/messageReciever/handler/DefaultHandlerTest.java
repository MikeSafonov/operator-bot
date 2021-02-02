package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.DefaultHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class DefaultHandlerTest {
    private final DefaultHandler defaultHandler = new DefaultHandler();

    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "nothing");
    private final String text = "Выберите действие.";

    @Test
    public void shouldReturnDefaultMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin();

        SendMessage actual = defaultHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithInternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal();

        SendMessage actual = defaultHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithExternal() {
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal();

        SendMessage actual = defaultHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithUnknown() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = defaultHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }
}
