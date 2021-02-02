package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	Optional<User> findByTelegramId(long telegramId);

	List<User> findAll();

	User addUserDuty(long telegramId, String full_name);

	User addUser(long telegramId, String full_name);

	void changeUserRole(long telegramId, Role role);

	void deleteUser(Integer id);

	Optional<User> findUserByUserStatusAndLastDutyDate();

	Optional<User> findFirstDutyOrderByFullName();

	User findById(Integer id);
}
