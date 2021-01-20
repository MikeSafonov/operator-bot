package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.ExternalUser;
import com.github.mikesafonov.operatorbot.repository.ExternalUserRepository;
import com.github.mikesafonov.operatorbot.service.ExternalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExternalUserServiceImpl implements ExternalUserService {

	private final ExternalUserRepository userRepository;

	@Override
	public Optional<ExternalUser> findByTelegramId(long telegramId) {
		return userRepository.findByTelegramId(telegramId);
	}
}
