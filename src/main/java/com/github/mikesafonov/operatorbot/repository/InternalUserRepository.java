package com.github.mikesafonov.operatorbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mikesafonov.operatorbot.model.InternalUser;

public interface InternalUserRepository extends JpaRepository<InternalUser, Integer> {

	Optional<InternalUser> findByTelegramId(long telegramId);
}
