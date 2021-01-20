package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.RoleHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RoleHandlerTest {
    private final RoleHandler roleHandler = new RoleHandler();

    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.ROLE, "/role");
    private final String adminText = "Вы администратор.";
    private final String internalUserText = "Вы внутренний пользователь.";
    private final String externalUserText = "Вы внешний пользователь.";
    private final String unknownUserText = "Мы вас не знаем.";

    @Test
    public void shouldReturnRoleMessageWithAdmin() {
        AuthorizationTelegram user = new AuthorizationTelegramAdmin();

        SendMessage actual = roleHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(adminText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithInternalUser() {
        AuthorizationTelegram user = new AuthorizationTelegramInternal();

        SendMessage actual = roleHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(internalUserText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithExternalUser() {
        AuthorizationTelegram user = new AuthorizationTelegramExternal();

        SendMessage actual = roleHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(externalUserText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithUnknownUser() {
        AuthorizationTelegram user = new AuthorizationTelegramUnknown();

        SendMessage actual = roleHandler.operate(chatId, user, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(unknownUserText);
        Assertions.assertEquals(expected, actual);
    }
}
