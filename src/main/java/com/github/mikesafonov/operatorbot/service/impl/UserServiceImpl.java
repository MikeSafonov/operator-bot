package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.exceptions.UserAlreadyExistException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.model.Status;
import com.github.mikesafonov.operatorbot.repository.UserRepository;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(String telegramId, String fullName) {
        if (userRepository.findByTelegramId(telegramId).isPresent()) {
            throw new UserAlreadyExistException("");
        } else {
            User user = new User();
            user.setTelegramId(telegramId);
            user.setFullName(fullName);
            user.setStatus(Status.ACTIVE);
            user.setChatStatus(ChatStatus.NONE);
            user.setRole(Role.DUTY);
            return userRepository.save(user);
        }
    }

    @Override
    public void changeUserRole(String telegramId, Role role) {
        userRepository.changeUserRole(telegramId, role);
    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public Optional<User> findByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    public Optional<User> findUserByUserStatusAndLastDutyDate() {
        return userRepository.findUserByUserStatusAndLastDutyDate();
    }

    @Override
    public Optional<User> findDutyByRoleByStatusOrderByFullNameAsc() {
        return userRepository.findDutyByRoleByStatusOrderByFullNameAsc();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist!"));
    }

    @Override
    public List<User> findByRoleAndStatusOrderByFullNameAsc(Role role, Status status) {
        return userRepository.findByRoleAndStatusOrderByFullNameAsc(role, status);
    }

    @Override
    public void updateUserChatStatus(String telegramId, ChatStatus chatStatus) {
        userRepository.updateUserChatStatus(telegramId, chatStatus);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
