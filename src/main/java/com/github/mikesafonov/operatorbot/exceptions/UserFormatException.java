package com.github.mikesafonov.operatorbot.exceptions;

public class UserFormatException  extends RuntimeException{
    public UserFormatException(String errorMessage) {
        super(errorMessage);
    }
}
