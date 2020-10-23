package com.github.mikesafonov.operatorbot.service.Impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.repository.InternalUserRepository;
import com.github.mikesafonov.operatorbot.service.InternalUserService;

@Service
public class InternalUserServiceImpl implements InternalUserService {

	private final InternalUserRepository userRepository;

	public InternalUserServiceImpl(InternalUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public InternalUser addUser(InternalUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<InternalUser> findByTelegramId(long telegramId) {
		return userRepository.findByTelegramId(telegramId);
	}

}
