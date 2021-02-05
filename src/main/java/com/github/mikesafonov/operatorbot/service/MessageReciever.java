package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.CommandHandlerFactory;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageReciever {
    private final AuthorizationService userAuthorization;
    private final Parser parser;
    private final CommandHandlerFactory commandHandlerFactory;
    private final MessageHandlerFactory messageHandlerFactory;

    public SendMessage analyze(Update update) {
        try {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            AuthorizationTelegram user = userAuthorization.getInfo(chatId);

            ParsedCommand parsedCommand = parser.getParsedCommand(message);
            MessageHandler messageHandler;

            if (user.isExternal()) {
                if (parsedCommand.getCommand().equals(Command.NONE)) {
                    messageHandler = getHandlerForMessage();
                } else {
                    messageHandler = getHandlerForCommand(parsedCommand.getCommand());
                }
            } else {
                messageHandler = getHandlerForCommand(parsedCommand.getCommand());
            }
            return messageHandler.operate(chatId, user, parsedCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return SendMessage.builder()
                    .chatId(Long.toString(update.getMessage().getChatId()))
                    .text("Произошла ошибка. Обратитесь к администратору")
                    .build();
        }
    }

    private MessageHandler getHandlerForCommand(Command command) {
        return commandHandlerFactory.createNewHandler(command);
    }

    private MessageHandler getHandlerForMessage() {
        return messageHandlerFactory.createNewHandler();
    }
}
