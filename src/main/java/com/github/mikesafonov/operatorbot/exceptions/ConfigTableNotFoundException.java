package com.github.mikesafonov.operatorbot.exceptions;

public class ConfigTableNotFoundException extends RuntimeException {
    public ConfigTableNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
