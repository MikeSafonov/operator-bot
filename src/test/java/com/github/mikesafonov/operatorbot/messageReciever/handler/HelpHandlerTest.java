package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.HelpHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUser;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpHandlerTest {
    private final HelpHandler helpHandler = new HelpHandler();

    private final User user = new User();
    private final String chatId = "0";
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.HELP, "/help");

    @BeforeEach
    public void setUp() {
        user.setFullName("User");
        user.setTelegramId(chatId);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.DUTY);
    }

    @Test
    void shouldReturnHelpMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin(user);

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(chatId)
                .text(buildAdminMessage())
                .parseMode(ParseMode.MARKDOWN)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHelpMessageWithUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUser(user);

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = SendMessage.builder()
                .chatId(chatId)
                .text(buildDutyMessage())
                .parseMode(ParseMode.MARKDOWN)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHelpMessageWithUnknownUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected =
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Обратитесь к администратору")
                        .parseMode(ParseMode.MARKDOWN)
                        .build();
        assertEquals(expected, actual);
    }

    private String buildAdminMessage() {
        return Arrays.stream(Command.values())
                .filter(Command::isAdmin)
                .map(Command::getDescription)
                .collect(Collectors.joining("\n"));
    }

    private String buildDutyMessage() {
        return Arrays.stream(Command.values())
                .filter(Command::isInternal)
                .map(Command::getDescription)
                .collect(Collectors.joining("\n"));
    }
}
