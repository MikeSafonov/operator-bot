package com.github.mikesafonov.operatorbot.service.impl;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.repository.TimetableRepository;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
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
	public Page<Timetable> findUsersDutyInFuture(InternalUser user, int amount) {
		return timetableRepository.findUsersDutyInFuture(user, PageRequest.of(0, amount));
	}

	@Override
	public void updateUserDate(LocalDate date, InternalUser user) {
		timetableRepository.updateUserDate(date, user);
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
