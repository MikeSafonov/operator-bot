package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.CommandHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class MessageReciever {
    private final AuthorizationService userAuthorization;
    private final Parser parser;
    private final CommandHandlerFactory commandHandlerFactory;

    public MessageReciever(AuthorizationService userAuthorization, Parser parser, CommandHandlerFactory commandHandlerFactory) {
        this.userAuthorization = userAuthorization;
        this.parser = parser;
        this.commandHandlerFactory = commandHandlerFactory;
    }

    public SendMessage analyze(Update update) {
        try {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            AuthorizationTelegram user = userAuthorization.getInfo(chatId);

            ParsedCommand parsedCommand = parser.getParsedCommand(message);
            MessageHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

            return handlerForCommand.operate(chatId, user, parsedCommand);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SendMessage(update.getMessage().getChatId(),  "Произошла ошибка. Обратитесь к администратору");
        }
    }

    private MessageHandler getHandlerForCommand(Command command) {
        return commandHandlerFactory.createNewHandler(command);
    }
}
