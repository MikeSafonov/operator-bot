package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationTelegramExternal implements AuthorizationTelegram {
    private final User user;

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isExternal() {
        return true;
    }

    @Override
    public boolean isInternal() {
        return false;
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

    @Override
    public ChatStatus getChatStatus() {
        return user.getChatStatus();
    }
}
