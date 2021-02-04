package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.ChatStatus;

public interface AuthorizationTelegram {
	boolean isAdmin();

	boolean isExternal();

	boolean isInternal();

	boolean isUnknown();

	String getUserFullName();

	long getTelegramId();

	ChatStatus getChatStatus();
}
