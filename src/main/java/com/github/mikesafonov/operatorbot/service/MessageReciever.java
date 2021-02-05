package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.CommandHandlerFactory;
import com.github.mikesafonov.operatorbot.model.ChatState;
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

    private ChatState chatState = new ChatState();

    public SendMessage analyzeMessage(Update update) {
        try {
            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            AuthorizationTelegram user = userAuthorization.getInfo(chatId);

            ParsedCommand parsedCommand = parser.getParsedCommand(message);

            return getMessageFromHandler(chatId, user, parsedCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return SendMessage.builder()
                    .chatId(Long.toString(update.getMessage().getChatId()))
                    .text("Произошла ошибка. Обратитесь к администратору")
                    .build();
        }
    }

    public SendMessage analyzeCallback(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String userId = update.getCallbackQuery().getData();

        chatState = new ChatState();
        chatState.setUserId(userId);
        chatState.setDutyId(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Введите ответ для пользователя.")
                .build();
    }

    private MessageHandler getHandlerForCommand(Command command) {
        return commandHandlerFactory.createNewHandler(command);
    }

    private MessageHandler getHandlerForMessage() {
        return messageHandlerFactory.createNewHandler();
    }

    private SendMessage getMessageFromHandler(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        if (user.isExternal()) {
            if (parsedCommand.getCommand().equals(Command.NONE)) {
                return getHandlerForMessage()
                        .operate(chatId, user, parsedCommand);
            } else {
                return getHandlerForCommand(parsedCommand.getCommand())
                        .operate(chatId, user, parsedCommand);
            }
        } else {
            if (parsedCommand.getCommand().equals(Command.NONE) && !"".equals(chatState.getUserId())) {
                chatState.setUserId("");
                chatState.setDutyId("");
                return SendMessage.builder()
                        .chatId(chatState.getUserId())
                        .text(parsedCommand.getText())
                        .build();
            } else {
                return getHandlerForCommand(parsedCommand.getCommand())
                        .operate(chatId, user, parsedCommand);
            }
        }
    }
}
