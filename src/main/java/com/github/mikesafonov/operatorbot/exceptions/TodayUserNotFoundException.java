package com.github.mikesafonov.operatorbot.exceptions;

public class TodayUserNotFoundException extends RuntimeException {

	public TodayUserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
