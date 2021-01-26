package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;

import java.util.List;
import java.util.Optional;

public interface InternalUserService {
	Optional<InternalUser> findByTelegramId(long telegramId);
	
	List<InternalUser> findAll();

	InternalUser addUser(long telegramId, String full_name) throws UserAlreadyExistException;

	void deleteUser(Integer id);

	InternalUser findUserByUserStatusAndLastDutyDate();

	InternalUser findById(Integer id) throws UserNotFoundException;
}
