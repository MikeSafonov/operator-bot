package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.ConfigTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfigTableRepository extends JpaRepository<ConfigTable, Integer> {
    Optional<ConfigTable> findByConfig(String config);
    @Modifying
    @Query("UPDATE ConfigTable u SET u.value = :value WHERE u.config = :config")
    void updateConfig(String config, String value);
}
