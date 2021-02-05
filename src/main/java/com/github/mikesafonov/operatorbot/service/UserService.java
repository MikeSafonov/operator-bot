package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByTelegramId(String telegramId);

    List<User> findAll();

    User addUser(String telegramId, String fullName);

    User addUserDuty(String telegramId, String fullName);

    void changeUserRole(String telegramId, Role role);

    void deleteUser(Integer id);

    Optional<User> findUserByUserStatusAndLastDutyDate();

    Optional<User> findDutyByRoleByStatusOrderByFullNameAsc();

    User findById(Integer id);

    List<User> findByRoleAndStatusOrderByFullNameAsc(Role role, Status status);

    void updateUserChatStatus(String telegramId, ChatStatus chatStatus);
}
