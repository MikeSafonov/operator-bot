package com.github.mikesafonov.operatorbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.mikesafonov.operatorbot.model.ExternalUser;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Integer> {

}
