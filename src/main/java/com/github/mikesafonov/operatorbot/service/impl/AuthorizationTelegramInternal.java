package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.ChatStatus;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationTelegramInternal implements AuthorizationTelegram {
    private final User user;

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public boolean isInternal() {
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
    public long getTelegramId() {
        return user.getTelegramId();
    }

    @Override
    public ChatStatus getChatStatus() {
        return user.getChatStatus();
    }
}
