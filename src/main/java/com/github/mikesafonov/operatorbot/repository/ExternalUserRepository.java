package com.github.mikesafonov.operatorbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mikesafonov.operatorbot.model.ExternalUser;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Integer> {
	Optional<ExternalUser> findByTelegramId(long telegramId);
}
