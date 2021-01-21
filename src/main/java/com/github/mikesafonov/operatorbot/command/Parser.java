package com.github.mikesafonov.operatorbot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Parser {
    public ParsedCommand getParsedCommand(String text) {
        if (text == null || text.equals("")) {
            return new ParsedCommand(Command.NONE, text);
        } else {
            return new ParsedCommand(getCommandFromText(text), text);
        }
    }

    private Command getCommandFromText(String text) {
        String upperCaseText = text.substring(1).toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            log.debug("Can't parse command: " + text);
        }
        return command;
    }
}
