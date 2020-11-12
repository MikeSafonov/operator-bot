package com.github.mikesafonov.operatorbot.exceptions;

public class UserNotFoundException extends Exception {
	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
