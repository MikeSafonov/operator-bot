package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.model.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface TimetableService {
	Timetable findByTodayDate() throws TodayUserNotFoundException;

	Optional<Timetable> findByDate(LocalDate date);

	Page<Timetable> findUsersDutyInFuture(User user, int amount);

	void updateUserDate(LocalDate date, User user);

	Timetable addNote(User user, LocalDate date) throws UserNotFoundException;
}
