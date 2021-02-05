package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageNotifier {
    private final TimetableService timetableService;
    private final MessageSender messageSender;

    @Scheduled(cron = "${notifyDutyCron}")
    public void getNotifyDuty() {
        try {
            Timetable timetable = timetableService.findByTodayDate();
            var message = SendMessage.builder()
                    .chatId(timetable.getUserId().getTelegramId())
                    .text(timetable.getUserId().getFullName() + ", ты сегодня дежурный!")
                    .build();

            messageSender.sendMessage(message);
        } catch (TodayUserNotFoundException e) {
            log.error("We have no duty users today!", e);
        }
    }
}
