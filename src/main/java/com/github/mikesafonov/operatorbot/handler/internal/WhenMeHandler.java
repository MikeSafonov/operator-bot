package com.github.mikesafonov.operatorbot.handler.internal;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.exceptions.ParserException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.handler.CommandHandler;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class WhenMeHandler implements CommandHandler {
    private final TimetableService timetableService;
    private final UserService userService;
    private final Parser parser;


    @Override
    public SendMessage operate(long chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageWhenUserOnDuty(chatId, parsedCommand.getText(), user);
    }

    private SendMessage getMessageWhenUserOnDuty(long chatId, String message, AuthorizationTelegram user) {
        StringBuilder text = new StringBuilder();
        if(user.isInternal()) {
                Integer amountOfDays = getValueFromMessage(message);
                List<Timetable> timetable = getListOfDuties(chatId, amountOfDays);
                if(timetable.size() > 0) {
                    text.append("Твои дежурства:").append("\n").append("\n");
                    for(int i = 0; i < timetable.size() && i < amountOfDays; i++) {
                        text.append(timetable.get(i).getTime().toString()).append("\n");
                    }
                } else {
                    text.append("Ближайшие дежурства не назначены!");
                }
        } else {
            text.append("Команда не доступна!");
        }
        return new SendMessage().setText(text.toString()).setChatId(chatId);
    }

    private List<Timetable> getListOfDuties(long chatId, int amount) {
        return userService.findByTelegramId(chatId)
                .map(user -> timetableService.findUsersDutyInFuture(user, amount))
                .map(Slice::getContent)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private Integer getValueFromMessage(String message) {
        try {
            return Integer.parseInt(parser.getParamValue(message, 0, 1));
        } catch (ParserException e) {
            return null;
        }
    }
}
