package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@RequiredArgsConstructor
public class StartChatHandler implements MessageHandler {
    private final UserService userService;

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getDutyChatMessage(chatId, user);
    }

    private SendMessage getDutyChatMessage(long chatId, AuthorizationTelegram user) {
        if (user.isUnknown()) {
            return SendMessage.builder()
                    .chatId(Long.toString(chatId))
                    .text("Команда не доступна!")
                    .build();
        } else {
            userService.updateUserChatStatus(chatId, ChatStatus.IN_CHAT);
            return SendMessage.builder()
                    .chatId(Long.toString(chatId))
                    .text("Введите сообщение дежурному.")
                    .build();
        }
    }
}
