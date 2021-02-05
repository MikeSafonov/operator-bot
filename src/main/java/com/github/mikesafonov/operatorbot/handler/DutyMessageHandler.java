package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DutyMessageHandler implements MessageHandler {
    private final TimetableService timetableService;

    @Override
    public SendMessage operate(String chatId, AuthorizationTelegram user, ParsedCommand parsedCommand) {
        return getMessageForDuty(chatId, user, parsedCommand.getText());
    }

    private SendMessage getMessageForDuty(String chatId, AuthorizationTelegram user, String message) {
        try {
            Timetable timetable = timetableService.findByTodayDate();
            String id = timetable.getUserId().getTelegramId();
            return SendMessage.builder()
                    .chatId(id)
                    .text(user.getUserFullName() + "\n\n" + message)
                    .replyMarkup(setButtonAnswer(chatId))
                    .build();

        } catch (TodayUserNotFoundException e) {
            log.error("Duty not found!", e);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Что-то пошло не так! Дежурный не на месте!")
                    .build();
        }
    }

    private ReplyKeyboard setButtonAnswer(String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Ответить");
        button.setCallbackData(chatId);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
