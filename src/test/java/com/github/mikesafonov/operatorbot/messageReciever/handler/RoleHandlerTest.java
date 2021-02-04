package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.RoleHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class RoleHandlerTest {
    private UserService userService;
    private final RoleHandler roleHandler = new RoleHandler();

    private final User user = new User();
    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.ROLE, "/role");
    private final String adminText = "Вы администратор.";
    private final String internalUserText = "Вы внутренний пользователь.";
    private final String externalUserText = "Вы внешний пользователь.";
    private final String unknownUserText = "Мы вас не знаем.";

    @BeforeEach
    public void setUp() {
        user.setTelegramId(chatId);
        user.setFullName("User");
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.DUTY);
        user.setChatStatus(ChatStatus.NONE);
    }

    @Test
    public void shouldReturnRoleMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin(user);

        SendMessage actual = roleHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(adminText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithInternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal(user);

        SendMessage actual = roleHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(internalUserText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithExternalUser() {
        user.setRole(Role.USER);
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal(user);

        SendMessage actual = roleHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(externalUserText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRoleMessageWithUnknownUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = roleHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(unknownUserText);
        Assertions.assertEquals(expected, actual);
    }
}
