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
    private Set<String> userId;

    private final UserService userService;

    public AuthorizationServiceImpl(UserService userService, @Value("${admin.list}") Set<String> userId) {
        this.userService = userService;
        this.userId = userId;

    }

    @Override
    public AuthorizationTelegram getInfo(String telegramId) {
        Optional<User> optionalUser = userService.findByTelegramId(telegramId);
        if (optionalUser.isPresent()) {
            if (userId.contains(telegramId)) {
                return new AuthorizationTelegramAdmin(optionalUser.get());
            } else {
                if (optionalUser.get().getRole().equals(Role.DUTY)) {
                    return new AuthorizationTelegramInternal(optionalUser.get());
                } else {
                    return new AuthorizationTelegramExternal(optionalUser.get());
                }
            }
        } else {
            return new AuthorizationTelegramUnknown();
        }
    }

}
