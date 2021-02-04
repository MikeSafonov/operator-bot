package com.github.mikesafonov.operatorbot.messageReciever;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.admin.AddHandler;
import com.github.mikesafonov.operatorbot.handler.command.*;
import com.github.mikesafonov.operatorbot.handler.command.admin.ReassignUsersHandler;
import com.github.mikesafonov.operatorbot.handler.command.admin.TimetableHandler;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageHandlerFactoryTest {
    @Mock
    private TimetableService timetableService;
    @Mock
    private UserService userService;
    @Mock
    private Parser parser;
    @Mock
    private DefinitionService definitionService;

    private CommandHandlerFactory commandHandlerFactory;

    @BeforeEach
    void setUp() {
        commandHandlerFactory = new CommandHandlerFactory(timetableService, userService, parser, definitionService);
    }

    @Test
    void shouldReturnDefaultHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.NONE);
        assertEquals(HelpHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnHelpHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.HELP);
        assertEquals(HelpHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnRoleHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.ROLE);
        assertEquals(RoleHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnStartHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.START);
        assertEquals(StartHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnWhoHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.WHO);
        assertEquals(WhoHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnAdminHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.ADD_USER);
        assertEquals(AddHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnReassignUsersHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.REASSIGN_DUTY);
        assertEquals(ReassignUsersHandler.class, actual.getClass());
    }

    @Test
    void shouldReturnTimetableHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.TIMETABLE);
        assertEquals(TimetableHandler.class, actual.getClass());
    }
}
