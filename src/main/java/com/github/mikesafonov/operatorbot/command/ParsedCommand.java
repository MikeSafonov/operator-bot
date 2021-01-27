package com.github.mikesafonov.operatorbot.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCommand {
    private Command command = Command.NONE;
    private String text = "";

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
