package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;

public class AuthorizationTelegramAdmin implements AuthorizationTelegram {

	@Override
	public boolean isExternal() {
		return false;
	}

	@Override
	public boolean isAdmin() {
		return true;
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
