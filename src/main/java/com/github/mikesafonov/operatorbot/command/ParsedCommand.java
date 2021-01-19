package com.github.mikesafonov.operatorbot.command;

import lombok.Data;

@Data
public class ParsedCommand {
    private Command command = Command.NONE;
    private String text = "";

    public ParsedCommand(Command command, String text) {
        this.command = command;
        this.text = text;
    }

    public boolean equals(Object anObject) {
        if(this == anObject) {
            return true;
        }
        if(anObject instanceof ParsedCommand) {
            ParsedCommand anotherParsedCommand = (ParsedCommand) anObject;
            String anotherText = anotherParsedCommand.getText();
            Command anotherCommand = anotherParsedCommand.getCommand();
            return anotherText.equals(text) && anotherCommand == command;
        }
        return false;
    }
}
