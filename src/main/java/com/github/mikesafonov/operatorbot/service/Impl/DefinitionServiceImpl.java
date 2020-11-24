package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.exceptions.UserNotFoundException;
import com.github.mikesafonov.operatorbot.model.AdditionalDayOff;
import com.github.mikesafonov.operatorbot.model.AdditionalWorkday;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.service.*;
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
public class DefinitionServiceImpl implements DefinitionService {

    private final TimetableService timetableService;
    private final InternalUserService internalUserService;
    private final AdditionalDayOffService additionalDayOffService;
    private final AdditionalWorkdayService additionalWorkdayService;
    private final Clock clock;

    Logger logger = LoggerFactory.getLogger(DefinitionServiceImpl.class.getName());

    public DefinitionServiceImpl(TimetableService timetableService,
                                 InternalUserService internalUserService,
                                 AdditionalDayOffService additionalDayOffService,
                                 AdditionalWorkdayService additionalWorkdayService, Clock clock) {
        this.timetableService = timetableService;
        this.internalUserService = internalUserService;
        this.additionalDayOffService = additionalDayOffService;
        this.additionalWorkdayService = additionalWorkdayService;
        this.clock = clock;
    }

    @Scheduled(cron = "${assignDutyCron}")
    @Override
    public void assignUser() {
        Optional<AdditionalDayOff> dayOff = additionalDayOffService.findByDay(LocalDate.now());
        Optional<AdditionalWorkday> workday = additionalWorkdayService.findByDay(LocalDate.now());
        dayOff.ifPresentOrElse((value) -> logger.debug("Today is day off!"), () -> {
            DayOfWeek dayOfWeek = LocalDate.now(clock).getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY || workday.isPresent()) {
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
            } else {
                logger.debug("Today is weekend!");
            }
        });
    }
}
