package com.github.mikesafonov.operatorbot.messageReciever;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.handler.command.admin.AddHandler;
import com.github.mikesafonov.operatorbot.handler.command.*;
import com.github.mikesafonov.operatorbot.handler.command.internal.WhoHandler;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MessageHandlerFactoryTest {
    @Mock
    private TimetableService timetableService;
    @Mock
    private UserService userService;
    @Mock
    private Parser parser;

    private CommandHandlerFactory commandHandlerFactory;

    @BeforeEach
    public void setUp() {
        commandHandlerFactory = new CommandHandlerFactory(timetableService, userService, parser);
    }

    @Test
    public void shouldReturnDefaultHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.NONE);
        Assertions.assertEquals(DefaultHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnHelpHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.HELP);
        Assertions.assertEquals(HelpHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnRoleHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.ROLE);
        Assertions.assertEquals(RoleHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnStartHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.START);
        Assertions.assertEquals(StartHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnWhoHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.WHO);
        Assertions.assertEquals(WhoHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnAdminHandler() {
        MessageHandler actual = commandHandlerFactory.createNewHandler(Command.ADD_USER);
        Assertions.assertEquals(AddHandler.class, actual.getClass());
    }
}
