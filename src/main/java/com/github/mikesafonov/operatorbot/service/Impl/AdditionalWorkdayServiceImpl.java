package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import com.github.mikesafonov.operatorbot.repository.AdditionalWorkdayRepository;
import com.github.mikesafonov.operatorbot.service.AdditionalWorkdayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdditionalWorkdayServiceImpl implements AdditionalWorkdayService {

    private final AdditionalWorkdayRepository repository;

    public AdditionalWorkdayServiceImpl(AdditionalWorkdayRepository repository) {
        this.repository = repository;
    }

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
