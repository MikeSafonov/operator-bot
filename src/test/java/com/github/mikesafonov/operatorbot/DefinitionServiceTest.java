package com.github.mikesafonov.operatorbot;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.github.mikesafonov.operatorbot.exceptions.TodayUserNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import com.github.mikesafonov.operatorbot.service.Impl.DefinitionServiceImpl;

public class DefinitionServiceTest {

	@Mock
	private TimetableService timetableService;
	@Mock
	private InternalUserService internalUserService;

	private DefinitionService service;
	private Timetable testUser;
	private LocalDate date = LocalDate.now();

	private Timetable unassigned = new Timetable();

	private LocalDate timetableDate = LocalDate.of(2020, 11, 5);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		service = new DefinitionServiceImpl(timetableService, internalUserService);
		testUser = new Timetable();
		testUser.setTime(date);
		unassigned.setTime(timetableDate);

	}

	@Test
	public void assignUserTestIfUserAssigned() {
		Timetable assigned = new Timetable();
		assigned.setTime(date);

		Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
		service.assignUser();
		Mockito.verify(timetableService, Mockito.times(0)).updateUserDate(LocalDate.now(), unassigned.getId());
	}

	@Test
	public void assignUserTestIfUserUnassigned() throws TodayUserNotFoundException {
		InternalUser user = new InternalUser();
		Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
		service.assignUser();
		try {
			Mockito.verify(timetableService, Mockito.times(1)).addNote(user.getId(), LocalDate.now());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void assignUserTestIfUserNotFound() {
		InternalUser user = new InternalUser();
		Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
		service.assignUser();
		Assertions.assertThatExceptionOfType(UserNotFoundException.class);
	}
}
