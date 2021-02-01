package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
	private Set<Long> userId;

	private final UserService userService;

	public AuthorizationServiceImpl(UserService userService, @Value("${admin.list}")Set<Long> userId) {
		this.userService = userService;
		this.userId = userId;

	}

	@Override
	public AuthorizationTelegram getInfo(long telegramId) {
		Optional<User> optionalUser = userService.findByTelegramId(telegramId);
		if (optionalUser.isPresent()) {
			if (userId.contains(telegramId)) {
				return new AuthorizationTelegramAdmin();
			}
			else {
				if(optionalUser.get().getRole().equals(Role.DUTY)) {
					return new AuthorizationTelegramInternal();
				} else {
					return new AuthorizationTelegramExternal();
				}
			}
		} else {
			return new AuthorizationTelegramUnknown();
		}
	}

}
