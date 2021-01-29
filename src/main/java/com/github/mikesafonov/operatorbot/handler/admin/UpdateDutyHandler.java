package com.github.mikesafonov.operatorbot.handler.admin;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.CommandFormatException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.CommandHandler;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@RequiredArgsConstructor
public class UpdateDutyHandler implements CommandHandler {
    private final TimetableService timetableService;
    private final InternalUserService internalUserService;
    private final Parser parser;

    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getDutyUpdatingMessage(chatId, parsedCommand.getText(), user);
    }

    private SendMessage getDutyUpdatingMessage(long chatId, String message, AuthorizationTelegram user) {
        StringBuilder text = new StringBuilder();
        if(user.isAdmin()) {
            try {
                updateDuty(message);
                text.append("Дежурный успешно обновлен!");
            } catch (UserNotFoundException e) {
                log.error("User with this id not found!", e);
                text.append("Пользователя с таким id не существует!");
            } catch (CommandFormatException e) {
                log.error("Incorrect command format", e);
                text.append("Команда введена неверно!");
            }
        } else {
            text.append("Команда не доступна!");
        }
        return new SendMessage().setChatId(chatId).setText(text.toString());
    }

    private void updateDuty(String message) {
        LocalDate date = getDateFromMessage(message);
        Long telegramId = getIdFromMessage(message);
        if(date != null && telegramId != null) {
            if(timetableService.findByDate(date).isPresent()) {
                updateDuty(date, telegramId);
            } else {
                addDuty(date, telegramId);
            }
        } else {
            throw new CommandFormatException("Incorrect command format!");
        }
    }

    private void addDuty(LocalDate date, long telegramId) {
        InternalUser user = internalUserService.findByTelegramId(telegramId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        timetableService.addNote(user.getId(), date);
    }

    private void updateDuty(LocalDate date, long telegramId) {
        InternalUser user = internalUserService.findByTelegramId(telegramId).orElseThrow(() -> new UserNotFoundException("User not found!"));
        timetableService.updateUserDate(date, user);
    }

    private LocalDate getDateFromMessage(String message) {
        try {
            return LocalDate.parse(parser.getParamValue(message, 0, 2));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Long getIdFromMessage(String message) {
        try {
            return Long.parseLong(parser.getParamValue(message, 1, 2));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
