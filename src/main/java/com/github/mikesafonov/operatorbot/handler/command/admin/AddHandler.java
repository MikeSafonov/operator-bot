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
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getAddingMessage(chatId, user, parsedCommand);
    }

    private SendMessage getAddingMessage(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (user.isAdmin()) {
            try {
                if(parsedCommand.getCommand().equals(Command.ADD_USER)) {
                    addUser(parsedCommand.getText());
                    sendMessage.setText("Пользователь успешно добавлен!");
                } else if(parsedCommand.getCommand().equals(Command.ADD_DUTY)) {
                    addDutyUser(parsedCommand.getText());
                    sendMessage.setText("Пользователь-дежурный успешно добавлен!");
                }
            } catch (UserAlreadyExistException e) {
                log.error("User with this id already exists!", e);
                sendMessage.setText("Пользователь с таким id уже существует!");
            } catch (CommandFormatException e) {
                log.error("Incorrect command format", e);
                sendMessage.setText("Команда введена неверно!");
            }
        } else {
            sendMessage.setText("Команда не доступна!");
        }
        return sendMessage;
    }

    private void addUser(String message) {
        Long id = getIdFromMessage(message);
        String fullName = getFullNameFromMessage(message);
        if (id != null && fullName != null) {
            userService.addUser(id, fullName);
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }
    }

    private void addDutyUser(String message) {
        Long id = getIdFromMessage(message);
        String fullName = getFullNameFromMessage(message);
        if (id != null && fullName != null) {
            userService.addUserDuty(id, fullName);
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }
    }

    private Long getIdFromMessage(String message) {
        try {
            return Long.parseLong(parser.getParamValue(message, 0, 2));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getFullNameFromMessage(String message) {
        return parser.getParamValue(message, 1, 2);
    }
}
