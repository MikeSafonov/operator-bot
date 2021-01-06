package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    public void shouldReturnRoleCommand() {
        ParsedCommand actual = parser.getParsedCommand("/role");
        ParsedCommand expected = new ParsedCommand(Command.ROLE, "/role");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStartCommand() {
        ParsedCommand actual = parser.getParsedCommand("/start");
        ParsedCommand expected = new ParsedCommand(Command.START, "/start");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHelpCommand() {
        ParsedCommand actual = parser.getParsedCommand("/help");
        ParsedCommand expected = new ParsedCommand(Command.HELP, "/help");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnWhoCommand() {
        ParsedCommand actual = parser.getParsedCommand("/who");
        ParsedCommand expected = new ParsedCommand(Command.WHO, "/who");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotReturnCommand() {
        ParsedCommand actual = parser.getParsedCommand("not a command");
        ParsedCommand expected = new ParsedCommand(Command.NONE, "not a command");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnUnknownCommand() {
        ParsedCommand actual = parser.getParsedCommand("/unknownCommand");
        ParsedCommand expected = new ParsedCommand(Command.NONE, "/unknownCommand");
        Assertions.assertEquals(expected, actual);
    }
}
