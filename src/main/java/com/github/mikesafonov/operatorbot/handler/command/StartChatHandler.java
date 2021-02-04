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
        SendMessage sendMessage = new SendMessage().setChatId(chatId);
        if (user.isUnknown()) {
            sendMessage.setText("Команда не доступна!");
        } else {
            userService.updateUserChatStatus(chatId, ChatStatus.IN_CHAT);
            sendMessage.setText("Введите сообщение дежурному.");
        }
        return sendMessage;
    }
}
