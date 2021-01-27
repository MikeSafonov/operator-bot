package com.github.mikesafonov.operatorbot.command;

import com.github.mikesafonov.operatorbot.exceptions.ParserException;
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

    public String getParamValue(String message, int paramIndex, int limit) {
        String[] text = getTextWithoutCommand(message).split(" ", limit);
        if(paramIndex < text.length && paramIndex >= 0) {
            return text[paramIndex];
        }
        else {
            return null;
        }
    }

    private Command getCommandFromText(String text) {
        String[] delimitedText = text.split(" ", 2);
        String upperCaseText = delimitedText[0].substring(1).toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            log.debug("Can't parse command: " + text);
        }
        return command;
    }

    private String getTextWithoutCommand(String text) {
        try {
            String[] delimitedText = text.split(" ", 2);
            return delimitedText[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParserException("Can't delimit text from command!");
        }
    }
}
