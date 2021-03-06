package com.github.mikesafonov.operatorbot.handler.command;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.admin.AddHandler;
import com.github.mikesafonov.operatorbot.handler.command.admin.ReassignUsersHandler;
import com.github.mikesafonov.operatorbot.handler.command.admin.TimetableHandler;
import com.github.mikesafonov.operatorbot.handler.command.admin.UpdateDutyHandler;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhenMeHandler;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHandlerFactory {
    private final TimetableService timetableService;
    private final UserService userService;
    private final Parser parser;
    private final DefinitionService definitionService;

    public MessageHandler createNewHandler(Command command) {
        switch (command) {
            case START:
                return new StartHandler();
            case ROLE:
                return new RoleHandler();
            case WHO:
                return new WhoHandler(timetableService);
            case ADD_USER:
                return new AddHandler(userService, parser);
            case REASSIGN_DUTY:
                return new ReassignUsersHandler(definitionService);
            case WHEN_MY_DUTY:
                return new WhenMeHandler(timetableService, userService, parser);
            case UPDATE_DUTY:
                return new UpdateDutyHandler(timetableService, userService, parser);
            case TIMETABLE:
                return new TimetableHandler(timetableService, parser);
            case HELP:
            default:
                return new HelpHandler();
        }
    }
}
