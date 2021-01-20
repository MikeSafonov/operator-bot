package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import com.github.mikesafonov.operatorbot.repository.AdditionalWorkdayRepository;
import com.github.mikesafonov.operatorbot.service.AdditionalWorkdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalWorkdayServiceImpl implements AdditionalWorkdayService {

    private final AdditionalWorkdayRepository repository;

    @Override
    public Page<AdditionalWorkday> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public AdditionalWorkday addNote(AdditionalWorkday note) {
        return repository.save(note);
    }

    @Override
    public Optional<AdditionalWorkday> findByDay(LocalDate workday) {
        return repository.findByWorkday(workday);
    }

    @Override
    public void deleteNote(Integer id) {
        repository.deleteById(id);
    }
}
