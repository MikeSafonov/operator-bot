package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface AdditionalWorkdayService {
    Page<AdditionalWorkday> findAll(Pageable pageable);

    AdditionalWorkday addNote(AdditionalWorkday note);

    Optional<AdditionalWorkday> findByDay(LocalDate Day);

    void deleteNote(Integer id);
}
