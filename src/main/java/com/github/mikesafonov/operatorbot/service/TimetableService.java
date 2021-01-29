package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface TimetableService {
	Timetable findByTodayDate() throws TodayUserNotFoundException;

	Optional<Timetable> findByDate(LocalDate date);

	Page<Timetable> findUsersDutyInFuture(InternalUser user, int amount);

	void updateUserDate(LocalDate date, InternalUser user);

	Timetable addNote(Integer userId, LocalDate date) throws UserNotFoundException;
}
