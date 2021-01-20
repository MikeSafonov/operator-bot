package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import com.github.mikesafonov.operatorbot.repository.AdditionalDayOffRepository;
import com.github.mikesafonov.operatorbot.service.AdditionalDayOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalDayOffServiceImpl implements AdditionalDayOffService {

    private final AdditionalDayOffRepository repository;

    @Override
    public Page<AdditionalDayOff> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public AdditionalDayOff addNote(AdditionalDayOff note) {
        return repository.save(note);
    }

    @Override
    public Optional<AdditionalDayOff> findByDay(LocalDate day) {
        return repository.findByDayOff(day);
    }

    @Override
    public void deleteNote(Integer id) {
        repository.deleteById(id);
    }
}
