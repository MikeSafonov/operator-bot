package com.github.mikesafonov.operatorbot.exceptions;

public class ParserException extends RuntimeException{
    public ParserException(String errorMessage) {
        super(errorMessage);
    }
}
