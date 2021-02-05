package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageHandlerFactory {
    private final TimetableService timetableService;
    private final UserService userService;

    public MessageHandler createNewHandler() {
        return new DutyMessageHandler(timetableService);
    }
}
