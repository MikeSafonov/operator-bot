package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface AdditionalDayOffService {
    Page<AdditionalDayOff> findAll(Pageable pageable);

    AdditionalDayOff addNote(AdditionalDayOff note);

    Optional<AdditionalDayOff> findByDay(LocalDate day);

    void deleteNote(Integer id);
}
