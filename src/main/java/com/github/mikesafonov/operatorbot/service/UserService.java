package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByTelegramId(String telegramId);

    User addUser(String telegramId, String fullName);

    Optional<User> findUserByUserStatusAndLastDutyDate();

    Optional<User> findDutyByRoleByStatusOrderByFullNameAsc();

    User findById(Integer id);
}
