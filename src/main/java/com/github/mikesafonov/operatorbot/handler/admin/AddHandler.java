package com.github.mikesafonov.operatorbot.handler.admin;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.CommandFormatException;
import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.handler.CommandHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Slf4j
public class AddHandler implements CommandHandler {
    private final InternalUserService internalUserService;
    private final Parser parser;

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getAddingMessage(chatId, user, parsedCommand.getText());
    }

    private SendMessage getAddingMessage(long chatId, AuthorizationTelegram user, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (user.isAdmin()) {
            try {
                addUser(message);
                sendMessage.setText("Пользователь успешно добавлен!");
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

    private void addUser(String message) throws UserAlreadyExistException {
        Long id = getIdFromMessage(message);
        String fullName = getFullNameFromMessage(message);
        if (id != null && fullName != null) {
            id.longValue();
            internalUserService.addUser(id, fullName);
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }

    }

    private Long getIdFromMessage(String message) {
        try {
            Long id = Long.parseLong(parser.getParamValue(message, 0, 2));
                return id;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getFullNameFromMessage(String message) {
        return parser.getParamValue(message, 1, 2);
    }
}
