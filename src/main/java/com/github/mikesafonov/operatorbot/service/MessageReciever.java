package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageReciever {
    private final AuthorizationService userAuthorization;
    private final Parser parser;
    private final CommandHandlerFactory commandHandlerFactory;

    private final static Logger logger = LoggerFactory.getLogger(MessageReciever.class.getName());

    public MessageReciever(AuthorizationService userAuthorization, Parser parser, CommandHandlerFactory commandHandlerFactory) {
        this.userAuthorization = userAuthorization;
        this.parser = parser;
        this.commandHandlerFactory = commandHandlerFactory;
    }

    public SendMessage analyze(Update update) {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        AuthorizationTelegram user = userAuthorization.getInfo(chatId);

        ParsedCommand parsedCommand = parser.getParsedCommand(message);
        CommandHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        SendMessage operationResult = handlerForCommand.operate(chatId, user, parsedCommand);
        return operationResult;
    }

    private CommandHandler getHandlerForCommand(Command command) {
        return commandHandlerFactory.createNewHandler(command);
    }
}
