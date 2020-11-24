package com.github.mikesafonov.operatorbot;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import com.github.mikesafonov.operatorbot.service.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.Impl.DefinitionServiceImpl;

public class DefinitionServiceTest {
    private final static LocalDate LOCAL_DATE_WEEKEND = LocalDate.of(2020, 11, 28); // weekend
    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 11, 27); // weekday

    @Mock
    private TimetableService timetableService;
    @Mock
    private InternalUserService internalUserService;
    @Mock
    private AdditionalDayOffService additionalDayOffService;
    @Mock
    private AdditionalWorkdayService additionalWorkdayService;
    @Mock
    private Clock clock;

    private final LocalDate date = LocalDate.now();
    private DefinitionService service;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new DefinitionServiceImpl(timetableService,
                internalUserService,
                additionalDayOffService,
                additionalWorkdayService, clock);
    }

    @Test
    public void assignUserTestIfUserAssignedAndWeekend() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        InternalUser firstUser = new InternalUser();
        InternalUser lastUser = new InternalUser();
        assigned.setUserId(firstUser);

        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(lastUser);
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser.getId(), date);
    }

    @Test
    public void assignUserTestIfUserAssignedAndWeekday() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        InternalUser firstUser = new InternalUser();
        InternalUser lastUser = new InternalUser();
        assigned.setUserId(firstUser);

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(lastUser);
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser.getId(), date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekend() {
        InternalUser user = new InternalUser();
        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        service.assignUser();
        try {
            Mockito.verify(timetableService, Mockito.times(0)).addNote(user.getId(), date);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekday() {
        InternalUser user = new InternalUser();
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        service.assignUser();
        try {
            Mockito.verify(timetableService, Mockito.times(1)).addNote(user.getId(), date);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignUserTestIfUserNotFoundAndWeekday() {
        InternalUser user = new InternalUser();

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        service.assignUser();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
    }

    @Test
    public void assignUserTestIfUserNotFoundAndWeekend() {
        InternalUser user = new InternalUser();

        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        service.assignUser();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
    }

    @Test
    public void assignUserTestIfUserAssignedAndDayOff() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        InternalUser firstUser = new InternalUser();
        InternalUser lastUser = new InternalUser();
        assigned.setUserId(firstUser);

        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(lastUser);
        Mockito.when(additionalDayOffService.findByDay(date)).thenReturn(Optional.of(new AdditionalDayOff()));
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser.getId(), date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndDayOffWithWeekday() throws UserNotFoundException {
        InternalUser user = new InternalUser();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        Mockito.when(additionalDayOffService.findByDay(date)).thenReturn(Optional.of(new AdditionalDayOff()));
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(user.getId(), date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekendButWorkday() throws UserNotFoundException {
        InternalUser user = new InternalUser();

        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(internalUserService.findUserByUserStatusAndLastDutyDate()).thenReturn(user);
        Mockito.when(additionalWorkdayService.findByDay(date)).thenReturn(Optional.of(new AdditionalWorkday()));
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(1)).addNote(user.getId(), date);
    }
}
