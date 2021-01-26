package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.repository.InternalUserRepository;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternalUserServiceImpl implements InternalUserService {

	private final InternalUserRepository userRepository;

	@Override
	public InternalUser addUser(long telegramId, String fullName) throws UserAlreadyExistException {
		if(userRepository.findByTelegramId(telegramId).isPresent()){
			throw new UserAlreadyExistException("");
		} else {
		InternalUser user = new InternalUser();
		user.setTelegramId(telegramId);
		user.setFullName(fullName);
		user.setStatus(Status.ACITVE);
		return userRepository.save(user);
		}
	}

	@Override
	public void deleteUser(Integer id) {

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
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User doesn't exist!"));
	}

	@Override
	public List<InternalUser> findAll() {
		return userRepository.findAll();
	}

}
