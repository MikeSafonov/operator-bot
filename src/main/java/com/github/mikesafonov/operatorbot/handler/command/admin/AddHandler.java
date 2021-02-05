package com.github.mikesafonov.operatorbot.handler.command.admin;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.CommandFormatException;
import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.handler.MessageHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Slf4j
public class AddHandler implements MessageHandler {
    private final UserService userService;
    private final Parser parser;

    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getAddingMessage(chatId, user, parsedCommand);
    }

    private SendMessage getAddingMessage(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        var builder = SendMessage.builder()
                .chatId(chatId);

        if (user.isAdmin()) {
            try {
                if (parsedCommand.getCommand().equals(Command.ADD_USER)) {
                    addUser(parsedCommand.getText());
                    return builder.text("Пользователь успешно добавлен!").build();
                } else if (parsedCommand.getCommand().equals(Command.ADD_DUTY)) {
                    addDutyUser(parsedCommand.getText());
                    return builder.text("Пользователь-дежурный успешно добавлен!").build();
                }
            } catch (UserAlreadyExistException e) {
                log.error("User with this id already exists!", e);
                return builder.text("Пользователь с таким id уже существует!").build();
            } catch (CommandFormatException e) {
                log.error("Incorrect command format", e);
                return builder.text("Команда введена неверно!").build();
            }
        }
        return builder.text("Команда не доступна!").build();
    }

    private void addUser(String message) {
        String id = getIdFromMessage(message);
        String fullName = getFullNameFromMessage(message);
        if (id != null && fullName != null) {
            userService.addUser(id, fullName);
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }
    }

    private void addDutyUser(String message) {
        String id = getIdFromMessage(message);
        String fullName = getFullNameFromMessage(message);
        if (id != null && fullName != null) {
            userService.addUserDuty(id, fullName);
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }
    }

    private String getIdFromMessage(String message) {
        try {
            return parser.getParamValue(message, 0, 2);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getFullNameFromMessage(String message) {
        return parser.getParamValue(message, 1, 2);
    }
}
