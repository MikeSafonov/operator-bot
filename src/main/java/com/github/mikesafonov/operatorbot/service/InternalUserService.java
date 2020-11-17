package com.github.mikesafonov.operatorbot.service;

import java.util.List;
import java.util.Optional;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;

public interface InternalUserService {
	Optional<InternalUser> findByTelegramId(long telegramId);
	
	List<InternalUser> findAll();

	InternalUser addUser(InternalUser user);

	void deleteUser(Integer id);

	InternalUser findUserByUserStatusAndLastDutyDate();

	InternalUser findById(Integer id) throws UserNotFoundException;
}
