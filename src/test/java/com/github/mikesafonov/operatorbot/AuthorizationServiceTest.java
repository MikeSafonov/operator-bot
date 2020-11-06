package com.github.mikesafonov.operatorbot;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.github.mikesafonov.operatorbot.model.ExternalUser;
import com.github.mikesafonov.operatorbot.model.InternalUser;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.ExternalUserService;
import com.github.mikesafonov.operatorbot.service.InternalUserService;
import com.github.mikesafonov.operatorbot.service.Impl.AuthorizationServiceImpl;

public class AuthorizationServiceTest {
	@Mock
	private InternalUserService internalService;
	@Mock
	private ExternalUserService externalService;

	private AuthorizationService service;

	Set<Long> adminList = new HashSet<>();
	long adminId = 000000;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		adminList.add(adminId);
		service = new AuthorizationServiceImpl(internalService, externalService, adminList);
	}

	@Test
	public void shouldReturnUnknownAuthorization() {
		long telegramId = 111111;
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertTrue(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertFalse(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(internalService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnInternalAuthorization() {
		long telegramId = 111111;

		Mockito.when(internalService.findByTelegramId(telegramId)).thenReturn(Optional.of(new InternalUser()));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertTrue(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(internalService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnExternalAuthorization() {
		long telegramId = 111111;

		Mockito.when(externalService.findByTelegramId(telegramId)).thenReturn(Optional.of(new ExternalUser()));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertTrue(auth.isExternal());
		Assertions.assertFalse(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(externalService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnAdminAuthorization() {
		long telegramId = 000000;

		Mockito.when(internalService.findByTelegramId(telegramId)).thenReturn(Optional.of(new InternalUser()));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertTrue(auth.isInternal());
		Assertions.assertTrue(auth.isAdmin());
		Mockito.verify(externalService, Mockito.times(1)).findByTelegramId(telegramId);

	}

}
