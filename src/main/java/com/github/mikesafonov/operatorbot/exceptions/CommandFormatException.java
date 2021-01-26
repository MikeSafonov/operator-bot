package com.github.mikesafonov.operatorbot.exceptions;

public class CommandFormatException extends RuntimeException{
    public CommandFormatException(String errorMessage) {
        super(errorMessage);
    }
}
