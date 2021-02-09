package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationTelegramAdmin implements AuthorizationTelegram {
    private final User user;

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public boolean isUser() {
        return true;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }

    @Override
    public String getUserFullName() {
        return user.getFullName();
    }

    @Override
    public String getTelegramId() {
        return user.getTelegramId();
    }
}
