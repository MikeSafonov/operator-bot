package com.github.mikesafonov.operatorbot.service;

import java.util.Optional;

import com.github.mikesafonov.operatorbot.model.InternalUser;

public interface InternalUserService {
	Optional<InternalUser> findByTelegramId(long telegramId);

	InternalUser addUser(InternalUser user);

	void deleteUser(Integer id);
}
