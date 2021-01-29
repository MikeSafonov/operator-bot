package com.github.mikesafonov.operatorbot.exceptions;

public class TimetableException extends RuntimeException {
    public TimetableException(String errorMessage) {
        super(errorMessage);
    }
}
