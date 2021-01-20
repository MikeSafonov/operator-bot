package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;

public class AuthorizationTelegramInternal implements AuthorizationTelegram {

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public boolean isExternal() {
		return false;
	}

	@Override
	public boolean isInternal() {
		return true;
	}

	@Override
	public boolean isUnknown() {
		return false;
	}

}
