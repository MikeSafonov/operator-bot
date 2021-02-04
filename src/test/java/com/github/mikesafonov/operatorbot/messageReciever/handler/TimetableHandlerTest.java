package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.Command;
import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.command.Parser;
import com.github.mikesafonov.operatorbot.handler.command.admin.TimetableHandler;
import com.github.mikesafonov.operatorbot.model.Timetable;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mike Safonov
 */
class TimetableHandlerTest {

    private TimetableService timetableService;
    private Parser parser;
    private TimetableHandler handler;
    private AuthorizationTelegram user;
    private long chatId = 0;

    @BeforeEach
    void setUp() {
        timetableService = mock(TimetableService.class);
        parser = new Parser();
        user = mock(AuthorizationTelegram.class);
        handler = new TimetableHandler(timetableService, parser);
    }

    @Nested
    class WhenAdmin {
        @BeforeEach
        void setUp() {
            when(user.isAdmin()).thenReturn(true);
        }

        @Test
        void shouldReturnCountWhenCountPassed() {
            var command = new ParsedCommand(Command.TIMETABLE, "/timetable 10");

            var timetable = new Timetable();
            timetable.setTime(LocalDate.of(2021, 1, 1));
            var userEntity = new User();
            userEntity.setFullName("FIO");
            timetable.setUserId(userEntity);
            Page<Timetable> page = new PageImpl(List.of(timetable));
            when(timetableService.findTimetableInFuture(10))
                    .thenReturn(page);

            var message = handler.operate(chatId, user, command);
            assertEquals("0", message.getChatId());
            assertEquals("2021-01-01 - FIO", message.getText());
        }

        @Test
        void shouldReturnExpected() {
            var command = new ParsedCommand(Command.TIMETABLE, "/timetable");

            var timetable = new Timetable();
            timetable.setTime(LocalDate.of(2021, 1, 1));
            var userEntity = new User();
            userEntity.setFullName("FIO");
            timetable.setUserId(userEntity);
            Page<Timetable> page = new PageImpl(List.of(timetable));
            when(timetableService.findTimetableInFuture(14))
                    .thenReturn(page);

            var message = handler.operate(chatId, user, command);
            assertEquals("0", message.getChatId());
            assertEquals("2021-01-01 - FIO", message.getText());
        }
    }

    @Nested
    class WhenNotAdmin {
        @BeforeEach
        void setUp() {
            when(user.isAdmin()).thenReturn(false);
        }

        @Test
        void shouldReturnNotAllowed() {
            var message = handler.operate(chatId, user, new ParsedCommand());
            assertEquals("0", message.getChatId());
            assertEquals("Команда не доступна!", message.getText());
        }
    }
}
