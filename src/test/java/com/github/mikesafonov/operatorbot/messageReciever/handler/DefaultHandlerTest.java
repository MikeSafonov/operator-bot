package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.DefaultHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.Impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.Impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.Impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.Impl.AuthorizationTelegramUnknown;
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
        AuthorizationTelegram user = new AuthorizationTelegramAdmin();

        SendMessage actual = defaultHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithInternalUser() {
        AuthorizationTelegram user = new AuthorizationTelegramInternal();

        SendMessage actual = defaultHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithExternal() {
        AuthorizationTelegram user = new AuthorizationTelegramExternal();

        SendMessage actual = defaultHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultMessageWithUnknown() {
        AuthorizationTelegram user = new AuthorizationTelegramUnknown();

        SendMessage actual = defaultHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text);
        Assertions.assertEquals(expected, actual);
    }
}
