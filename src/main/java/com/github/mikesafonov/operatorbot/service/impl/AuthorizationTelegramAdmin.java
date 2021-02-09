package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationTelegramAdmin implements AuthorizationTelegram {
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
}
