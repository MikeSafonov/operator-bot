package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.admin.AddHandler;
import com.github.mikesafonov.operatorbot.handler.internal.WhenMeHandler;
import com.github.mikesafonov.operatorbot.handler.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHandlerFactory {
    private final TimetableService timetableService;
    private final InternalUserService internalUserService;
    private final Parser parser;

    public CommandHandler createNewHandler(Command command) {
        switch (command) {
            case START:
                return new StartHandler();
            case HELP:
                return new HelpHandler();
            case ROLE:
                return new RoleHandler();
            case WHO:
                return new WhoHandler(timetableService);
            case ADD:
                return new AddHandler(internalUserService, parser);
            case WHEN_MY_DUTY:
                return new WhenMeHandler(timetableService, internalUserService, parser);
            default:
                return new DefaultHandler();
        }
    }
}
