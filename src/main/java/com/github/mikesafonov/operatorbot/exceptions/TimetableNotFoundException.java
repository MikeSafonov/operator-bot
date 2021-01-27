package com.github.mikesafonov.operatorbot.exceptions;

public class TimetableNotFoundException extends RuntimeException {
    public TimetableNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
