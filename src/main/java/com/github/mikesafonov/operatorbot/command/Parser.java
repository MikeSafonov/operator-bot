package com.github.mikesafonov.operatorbot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class.getName());

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
            logger.debug("Can't parse command: " + text);
        }
        return command;
    }
}
