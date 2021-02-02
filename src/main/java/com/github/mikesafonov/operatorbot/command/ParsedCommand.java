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
}
