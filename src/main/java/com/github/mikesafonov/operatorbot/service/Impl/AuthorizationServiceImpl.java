package com.github.mikesafonov.operatorbot.service.Impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.mikesafonov.operatorbot.model.ExternalUser;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.ExternalUserService;
import com.github.mikesafonov.operatorbot.service.InternalUserService;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
	@Value("${admin.list}")
	private Set<Long> userId;

	private final InternalUserService internalUserService;
	private final ExternalUserService externalUserService;

	public AuthorizationServiceImpl(InternalUserService internalUserService, ExternalUserService externalUserService) {
		this.internalUserService = internalUserService;
		this.externalUserService = externalUserService;

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
