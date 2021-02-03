package com.github.mikesafonov.operatorbot.messageReciever.handler;

import com.github.mikesafonov.operatorbot.command.ParsedCommand;
import com.github.mikesafonov.operatorbot.handler.command.admin.ReassignUsersHandler;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.DefinitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class ReassignUsersHandlerTest {
    private DefinitionService definitionService;
    private ReassignUsersHandler handler;
    private long chatId = 0;
    private AuthorizationTelegram user;
    private ParsedCommand command;

    @BeforeEach
    void setUp() {
        definitionService = mock(DefinitionService.class);
        user = mock(AuthorizationTelegram.class);
        handler = new ReassignUsersHandler(definitionService);
    }

    @Nested
    class WhenAdmin {
        @BeforeEach
        void setUp() {
            when(user.isAdmin()).thenReturn(true);
        }

        @Test
        void shouldReassignDuty() {
            handler.operate(chatId, user, command);

            verify(definitionService).assignUser();
        }

        @Test
        void shouldReturnExpectedMessage() {
            var message = handler.operate(chatId, user, command);

            assertEquals("0", message.getChatId());
            assertEquals("Дежурные назначены!", message.getText());
        }
    }

    @Nested
    class WhenNotAdmin {

        @BeforeEach
        void setUp() {
            when(user.isAdmin()).thenReturn(false);
        }

        @Test
        void shouldReturnExpectedMessage() {
            var message = handler.operate(chatId, user, command);
            assertEquals("0", message.getChatId());
            assertEquals("Команда не доступна!", message.getText());
        }
    }

}
