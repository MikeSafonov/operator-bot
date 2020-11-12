package com.github.mikesafonov.operatorbot.service;

import java.time.LocalDate;
import java.util.Optional;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.Timetable;

public interface TimetableService {
	Timetable findByTodayDate() throws TodayUserNotFoundException;

	Optional<Timetable> findByDate(LocalDate date);

	void updateUserDate(LocalDate date, Integer id);

	Timetable addNote(Integer userId, LocalDate date) throws UserNotFoundException;
}
