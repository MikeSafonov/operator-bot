package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageNotifierTest {

    @Mock
    private TimetableService timetableService;
    @Mock
    private MessageSender messageSender;
    private MessageNotifier messageNotifier;

    private Timetable timetable = new Timetable();
    private InternalUser duty = new InternalUser();
    private SendMessage sendMessage = new SendMessage();
    private long id;
    private String message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        messageNotifier = new MessageNotifier(timetableService, messageSender);
        duty.setTelegramId(0);
        duty.setFullName("Duty");
        duty.setStatus(Status.ACTIVE);
        timetable.setUserId(duty);
        id = duty.getTelegramId();
        message = (timetable.getUserId().getFullName() + ", ты сегодня дежурный!");
    }

    @Test
    public void shouldNotifyDuty() throws TodayUserNotFoundException {
        Mockito.when(timetableService.findByTodayDate()).thenReturn(timetable);
        sendMessage.setChatId(id);
        sendMessage.setText(message);
        messageNotifier.getNotifyDuty();
        Mockito.verify(messageSender, Mockito.times(1)).sendMessage(sendMessage);
    }

    @Test
    public void shouldNotNotifyDuty() throws TodayUserNotFoundException {
        Mockito.when(timetableService.findByTodayDate()).thenThrow(new TodayUserNotFoundException("We don't have duty today!"));
        sendMessage.setChatId(id);
        sendMessage.setText(message);
        messageNotifier.getNotifyDuty();
        Mockito.verify(messageSender, Mockito.times(0)).sendMessage(sendMessage);
    }
}
