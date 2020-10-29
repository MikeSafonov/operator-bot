package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;

public class AuthorizationTelegramExternal implements AuthorizationTelegram {

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public boolean isExternal() {
		return true;
	}

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public boolean isUnknown() {
		return false;
	}

}
