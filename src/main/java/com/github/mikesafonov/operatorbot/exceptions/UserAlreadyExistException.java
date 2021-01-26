package com.github.mikesafonov.operatorbot.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
