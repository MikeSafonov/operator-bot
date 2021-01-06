package com.github.mikesafonov.operatorbot.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.springframework.stereotype.Component;

@Component
public class CommandHandlerFactory {
    private final TimetableService timetableService;

    public CommandHandlerFactory(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

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
            default:
                return new DefaultHandler();
        }
    }
}
