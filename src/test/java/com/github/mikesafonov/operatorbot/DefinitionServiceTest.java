package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.*;
import com.github.mikesafonov.operatorbot.service.*;
import com.github.mikesafonov.operatorbot.service.impl.DefinitionServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class DefinitionServiceTest {
    private final static LocalDate LOCAL_DATE_WEEKEND = LocalDate.of(2020, 11, 28); // weekend (Saturday)
    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 11, 23); // weekday (Monday)

    @Mock
    private TimetableService timetableService;
    @Mock
    private UserService userService;
    @Mock
    private AdditionalDayOffService additionalDayOffService;
    @Mock
    private AdditionalWorkdayService additionalWorkdayService;
    @Mock
    private ConfigTableService configService;
    @Mock
    private Clock clock;

    private final LocalDate date = LocalDate.now();
    private DefinitionService service;
    private Clock fixedClock;

    private final User user = new User();
    private final User lastUser = new User();
    private final ConfigTable configTable = new ConfigTable();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new DefinitionServiceImpl(timetableService,
                userService,
                additionalDayOffService,
                additionalWorkdayService, configService, clock);
    }

    @Test
    public void assignUserTestIfUserAssignedAndWeekend() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        configTable.setValue("0");
        assigned.setUserId(user);

        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(lastUser));

        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser, date);
    }

    @Test
    public void assignUserTestIfUserAssignedAndWeekday() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        configTable.setValue("0");
        assigned.setUserId(user);

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(lastUser));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser, date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekend() {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        service.assignUser();
        try {
            Mockito.verify(timetableService, Mockito.times(0)).addNote(user, date);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekday() {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        service.assignUser();
        for(int i = 0; i < Integer.parseInt(configTable.getValue()); i++) {
            try {
                Mockito.verify(timetableService, Mockito.times(1)).addNote(user, LocalDate.now(fixedClock).plusDays(i));
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void assignUserForFewDaysTestIfUserUnassignedAndWeekday() {
        configTable.setValue("4");
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);

        service.assignUser();
        for(int i = 0; i < Integer.parseInt(configTable.getValue()); i++) {
            try {
                Mockito.verify(timetableService, Mockito.times(1)).addNote(user, LocalDate.now(fixedClock).plusDays(i));
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void assignUserTestIfUserNotFoundAndWeekday() {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);

        service.assignUser();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
    }

    @Test
    public void assignUserTestIfUserNotFoundAndWeekend() {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);

        service.assignUser();
        Assertions.assertThatExceptionOfType(UserNotFoundException.class);
    }

    @Test
    public void assignUserTestIfUserAssignedAndDayOff() throws UserNotFoundException {
        Timetable assigned = new Timetable();
        configTable.setValue("0");
        assigned.setUserId(user);
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(timetableService.findByDate(date)).thenReturn(Optional.of(assigned));
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(lastUser));
        Mockito.when(additionalDayOffService.findByDay(date)).thenReturn(Optional.of(new AdditionalDayOff()));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);

        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(lastUser, date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndDayOffWithWeekday() throws UserNotFoundException {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(additionalDayOffService.findByDay(LocalDate.now(fixedClock))).thenReturn(Optional.of(new AdditionalDayOff()));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);

        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(0)).addNote(user, date);
    }

    @Test
    public void assignUserTestIfUserUnassignedAndWeekendButWorkday() throws UserNotFoundException {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findUserByUserStatusAndLastDutyDate()).thenReturn(Optional.of(user));
        Mockito.when(additionalWorkdayService.findByDay(LocalDate.now(fixedClock))).thenReturn(Optional.of(new AdditionalWorkday()));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        Mockito.when(userService.findById(user.getId())).thenReturn(user);

        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(1)).addNote(user, LocalDate.now(fixedClock));
    }

    @Test
    public void assignUserTestIfIsFirstDuty() throws UserNotFoundException {
        configTable.setValue("0");
        fixedClock = Clock.fixed(LOCAL_DATE_WEEKEND.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        Mockito.when(userService.findFirstOrderByFullName()).thenReturn(Optional.of(user));
        Mockito.when(additionalWorkdayService.findByDay(LocalDate.now(fixedClock))).thenReturn(Optional.of(new AdditionalWorkday()));
        Mockito.when(configService.findByConfig("configAdditionalDays")).thenReturn(configTable);
        Mockito.when(userService.findById(user.getId())).thenReturn(user);

        service.assignUser();
        Mockito.verify(timetableService, Mockito.times(1)).addNote(user, LocalDate.now(fixedClock));
    }
}
