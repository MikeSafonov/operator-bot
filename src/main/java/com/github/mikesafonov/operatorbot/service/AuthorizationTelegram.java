package com.github.mikesafonov.operatorbot.service;

public interface AuthorizationTelegram {
	boolean isAdmin();

	boolean isExternal();

	boolean isInternal();

	boolean isUnknown();
}
