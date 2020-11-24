package com.github.mikesafonov.operatorbot.repository;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AdditionalDayOffRepository extends JpaRepository<AdditionalDayOff, Integer> {
    Optional<AdditionalDayOff> findByDayOff(LocalDate dayOff);
    Page<AdditionalDayOff> findAll(Pageable page);
}
