package com.github.mikesafonov.operatorbot.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
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

	@Override
	public InternalUser findUserByUserStatusAndLastDutyDate() {
		return userRepository.findUserByUserStatusAndLastDutyDate();
	}

	@Override
	public InternalUser findById(Integer id) throws UserNotFoundException {
		InternalUser user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User doesn't exist!"));
		return user;
	}

	@Override
	public List<InternalUser> findAll() {
		return userRepository.findAll();
	}

}
