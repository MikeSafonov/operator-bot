package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.repository.TimetableRepository;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {

	private final TimetableRepository timetableRepository;
	private final InternalUserService internalUserService;

	@Override
	public Timetable findByTodayDate() throws TodayUserNotFoundException {
		return timetableRepository.findByTime((LocalDate.now()))
				.orElseThrow(() -> new TodayUserNotFoundException("User not found!"));
	}

	@Override
	public Optional<Timetable> findByDate(LocalDate date) {
		return timetableRepository.findByTime(date);
	}

	@Override
	public void updateUserDate(LocalDate date, Integer id) {
		timetableRepository.updateUserDate(date, id);

	}

	@Override
	public Timetable addNote(Integer userId, LocalDate date) throws UserNotFoundException {
		Timetable newNote = new Timetable();
		InternalUser user = internalUserService.findById(userId);
		newNote.setUserId(user);
		newNote.setTime(date);
		return timetableRepository.save(newNote);
	}
}