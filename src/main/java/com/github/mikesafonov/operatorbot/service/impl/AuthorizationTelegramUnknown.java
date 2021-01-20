package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;

public class AuthorizationTelegramUnknown implements AuthorizationTelegram {

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
		return false;
	}

	@Override
	public boolean isUnknown() {
		return true;
	}

}
