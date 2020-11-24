package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import com.github.mikesafonov.operatorbot.repository.AdditionalDayOffRepository;
import com.github.mikesafonov.operatorbot.service.AdditionalDayOffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdditionalDayOffServiceImpl implements AdditionalDayOffService {

    private final AdditionalDayOffRepository repository;

    public AdditionalDayOffServiceImpl(AdditionalDayOffRepository repository) {
        this.repository = repository;
    }

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
