package com.github.mikesafonov.operatorbot.service;

public interface AuthorizationTelegram {
	boolean isAdmin();

	boolean isUser();

	boolean isUnknown();
}
