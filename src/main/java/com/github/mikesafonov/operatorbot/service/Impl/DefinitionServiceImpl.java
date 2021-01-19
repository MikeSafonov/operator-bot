package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.exceptions.ConfigTableNotFoundException;
import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.*;
import com.github.mikesafonov.operatorbot.service.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DefinitionServiceImpl implements DefinitionService {

    private final TimetableService timetableService;
    private final InternalUserService internalUserService;
    private final AdditionalDayOffService additionalDayOffService;
    private final AdditionalWorkdayService additionalWorkdayService;
    private final ConfigTableService configService;
    private final Clock clock;

    @Scheduled(cron = "${assignDutyCron}")
    @Override
    public void assignUser() {
        int additionalDaysForAssign = 0;
        try {
            additionalDaysForAssign = Integer.parseInt(configService.findByConfig("configAdditionalDays").getValue());
        } catch (ConfigTableNotFoundException e) {
            log.error("Configuration not found!", e);
        }
        for (int i = 0; i <= additionalDaysForAssign; i++) {
            LocalDate date = LocalDate.now(clock).plusDays(i);
            Optional<AdditionalDayOff> dayOff = additionalDayOffService.findByDay(date);
            dayOff.ifPresentOrElse((value) -> log.debug(date.toString() + " is day off!"), () -> {
                if (isWorday(date)) {
                    Optional<Timetable> timetable = timetableService.findByDate(date);
                    timetable.ifPresentOrElse((value) -> {
                        log.debug("User: " + timetable.get().getUserId().getFullName() + " has already been assigned! Date is " + date.toString());
                    }, () -> {
                        InternalUser user = internalUserService.findUserByUserStatusAndLastDutyDate();
                        try {
                            timetableService.addNote(user.getId(), date);
                            log.debug("User: " + user.getFullName() + " is assigned! Date is " + date.toString());
                        } catch (UserNotFoundException e) {
                            e.printStackTrace();
                            log.error("Expected user for assigning not found!", e);
                        }
                    });
                } else {
                    log.debug(date.toString() + " is weekend!");
                }
            });
        }
    }

    public boolean isWorday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY ) {
            Optional<AdditionalWorkday> workday = additionalWorkdayService.findByDay(date);
            return workday.isPresent();
        }
        return true;
    }
}
