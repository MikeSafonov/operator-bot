package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.HelpHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramAdmin;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramExternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramInternal;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationTelegramUnknown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpHandlerTest {
    private final HelpHandler helpHandler = new HelpHandler();

    private final StringBuilder text = new StringBuilder();
    private final long chatId = 0;
    private final ParsedCommand parsedCommand = new ParsedCommand(Command.HELP, "/help");

    @BeforeEach
    public void setUp() {
        String END_LINE = "\n";
        text.append("*Список основных сообщений*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - Показать стартовое сообщение.").append(END_LINE);
        text.append("[/help](/help) - Помощь.").append(END_LINE);
        text.append("[/role](/role) - Узнать свою роль.").append(END_LINE);
    }

    @Test
    public void shouldReturnHelpMessageWithAdmin() {
        AuthorizationTelegram authorization = new AuthorizationTelegramAdmin();

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHelpMessageWithInternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramInternal();

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHelpMessageWithExternalUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramExternal();

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHelpMessageWithUnknownUser() {
        AuthorizationTelegram authorization = new AuthorizationTelegramUnknown();

        SendMessage actual = helpHandler.operate(chatId, authorization, parsedCommand);
        SendMessage expected = new SendMessage().setChatId(chatId).setText(text.toString());
        expected.enableMarkdown(true);
        Assertions.assertEquals(expected, actual);
    }
}
