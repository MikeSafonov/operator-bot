package com.github.mikesafonov.operatorbot.service;

import java.util.Optional;

import com.github.mikesafonov.operatorbot.model.ExternalUser;

public interface ExternalUserService {
	Optional<ExternalUser> findByTelegramId(long telegramId);
}
