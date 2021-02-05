package com.github.mikesafonov.operatorbot.handler.command.admin;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Mike Safonov
 */
@RequiredArgsConstructor
public class ReassignUsersHandler implements MessageHandler {

    private final DefinitionService definitionService;

    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        if (user.isAdmin()) {
            definitionService.assignUser();
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Дежурные назначены!")
                    .build();
        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Команда не доступна!")
                    .build();
        }
    }
}
