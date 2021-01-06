package com.github.mikesafonov.operatorbot.messageReciever;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.handler.*;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class CommandHandlerFactoryTest {
    @Mock
    private TimetableService timetableService;

    private CommandHandlerFactory commandHandlerFactory;

    @BeforeEach
    public void setUp() {
        commandHandlerFactory = new CommandHandlerFactory(timetableService);
    }

    @Test
    public void shouldReturnDefaultHandler() {
        CommandHandler actual = commandHandlerFactory.createNewHandler(Command.NONE);
        Assertions.assertEquals(DefaultHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnHelpHandler() {
        CommandHandler actual = commandHandlerFactory.createNewHandler(Command.HELP);
        Assertions.assertEquals(HelpHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnRoleHandler() {
        CommandHandler actual = commandHandlerFactory.createNewHandler(Command.ROLE);
        Assertions.assertEquals(RoleHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnStartHandler() {
        CommandHandler actual = commandHandlerFactory.createNewHandler(Command.START);
        Assertions.assertEquals(StartHandler.class, actual.getClass());
    }

    @Test
    public void shouldReturnWhoHandler() {
        CommandHandler actual = commandHandlerFactory.createNewHandler(Command.WHO);
        Assertions.assertEquals(WhoHandler.class, actual.getClass());
    }
}
