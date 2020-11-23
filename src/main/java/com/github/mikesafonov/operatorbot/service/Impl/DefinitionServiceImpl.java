package com.github.mikesafonov.operatorbot.service.Impl;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	Logger logger = LoggerFactory.getLogger(DefinitionServiceImpl.class.getName());

	public DefinitionServiceImpl(TimetableService timetableService, InternalUserService internalUserService) {
		this.timetableService = timetableService;
		this.internalUserService = internalUserService;
	}

	@Scheduled(cron = "${assignDutyCron}")
	@Override
	public void assignUser() {
		Optional<Timetable> timetable = timetableService.findByDate(LocalDate.now());
		timetable.ifPresentOrElse((value) -> {
			logger.debug("User: " + timetable.get().getUserId().getFullName() + " has already been assigned!");
		}, () -> {
			InternalUser user = internalUserService.findUserByUserStatusAndLastDutyDate();
			try {
				timetableService.addNote(user.getId(), LocalDate.now());
				logger.debug("User: " + user.getFullName() + " is assigned!");
			} catch (UserNotFoundException e) {
				e.printStackTrace();
				logger.error("Expected user for assigning not found!", e);
			}
		});
	}
}
