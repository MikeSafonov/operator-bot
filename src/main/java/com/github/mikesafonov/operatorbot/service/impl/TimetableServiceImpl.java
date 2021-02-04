package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserFormatException;
import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.repository.TimetableRepository;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;
    private final UserService userService;

    @Override
    public Timetable findByTodayDate() {
        return timetableRepository.findByTime((LocalDate.now()))
                .orElseThrow(() -> new TodayUserNotFoundException("User not found!"));
    }

    @Override
    public Optional<Timetable> findByDate(LocalDate date) {
        return timetableRepository.findByTime(date);
    }

    @Override
    public Page<Timetable> findUsersDutyInFuture(User user, int amount) {
        return timetableRepository.findUsersDutyInFuture(user, PageRequest.of(0, amount));
    }

    @Override
    public void updateUserDate(LocalDate date, User user) {
        if (user.getRole().equals(Role.DUTY)) {
            timetableRepository.updateUserDate(date, user);
        } else {
            throw new UserFormatException("User can not be duty!");
        }
    }

    @Override
    public Timetable addNote(User user, LocalDate date) {
        Timetable newNote = new Timetable();
        if (user.getRole().equals(Role.DUTY)) {
            newNote.setUserId(user);
            newNote.setTime(date);
            return timetableRepository.save(newNote);
        } else {
            throw new UserFormatException("User can not be duty!");
        }
    }
}
