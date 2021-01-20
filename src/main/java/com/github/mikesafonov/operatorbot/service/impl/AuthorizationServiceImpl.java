package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.ExternalUser;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.ExternalUserService;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
	private Set<Long> userId;

	private final InternalUserService internalUserService;
	private final ExternalUserService externalUserService;

	public AuthorizationServiceImpl(InternalUserService internalUserService, ExternalUserService externalUserService, @Value("${admin.list}")Set<Long> userId) {
		this.internalUserService = internalUserService;
		this.externalUserService = externalUserService;
		this.userId = userId;

	}

	@Override
	public AuthorizationTelegram getInfo(long telegramId) {
		Optional<InternalUser> optionalInternalUser = internalUserService.findByTelegramId(telegramId);
		Optional<ExternalUser> optionalExternalUser = externalUserService.findByTelegramId(telegramId);
		if (optionalInternalUser.isPresent()) {
			if (userId.contains(telegramId)) {
				return new AuthorizationTelegramAdmin();
			}
			else {
				return new AuthorizationTelegramInternal();
			}
		} else if (optionalExternalUser.isPresent()) {
			return new AuthorizationTelegramExternal();
		} else {
			return new AuthorizationTelegramUnknown();
		}
	}

}
