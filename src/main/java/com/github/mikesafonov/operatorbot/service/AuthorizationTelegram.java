package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.ChatStatus;

public interface AuthorizationTelegram {
	boolean isAdmin();

	boolean isUser();

	boolean isUnknown();

	String getUserFullName();

	String getTelegramId();
}
