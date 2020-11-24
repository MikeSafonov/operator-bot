package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AdditionalWorkdayRepository extends JpaRepository<AdditionalWorkday, Integer> {
    Optional<AdditionalWorkday> findByWorkday(LocalDate workday);
}
