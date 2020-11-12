package com.github.mikesafonov.operatorbot.service.Impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;

@Service
@Transactional
public class DefinitionServiceImpl implements DefinitionService {

	private final TimetableService timetableService;
	private final InternalUserService internalUserService;

	public DefinitionServiceImpl(TimetableService timetableService, InternalUserService internalUserService) {
		this.timetableService = timetableService;
		this.internalUserService = internalUserService;
	}

	@Scheduled(cron = "${assignDutyCron}")
	@Override
	public void assignUser() {
		Optional<Timetable> timetable = timetableService.findByDate(LocalDate.now());
		timetable.ifPresentOrElse((value) -> {
		}, () -> {
			InternalUser user = internalUserService.findUserByUserStatusAndLastDutyDate();
			try {
				timetableService.addNote(user.getId(), LocalDate.now());
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
}
