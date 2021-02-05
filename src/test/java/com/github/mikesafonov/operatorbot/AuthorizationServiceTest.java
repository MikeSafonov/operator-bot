package com.github.mikesafonov.operatorbot;

import com.github.mikesafonov.operatorbot.model.Role;
import com.github.mikesafonov.operatorbot.model.User;
import com.github.mikesafonov.operatorbot.service.AuthorizationService;
import com.github.mikesafonov.operatorbot.service.AuthorizationTelegram;
import com.github.mikesafonov.operatorbot.service.UserService;
import com.github.mikesafonov.operatorbot.service.impl.AuthorizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthorizationServiceTest {
	@Mock
	private UserService userService;

	private AuthorizationService service;

	Set<String> adminList = new HashSet<>();
	String adminId = "000000";

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		adminList.add(adminId);
		service = new AuthorizationServiceImpl(userService, adminList);
	}

	@Test
	public void shouldReturnUnknownAuthorization() {
		String telegramId = "111111";
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertTrue(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertFalse(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(userService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnInternalAuthorization() {
		String telegramId = "111111";
		User user = new User();
		user.setRole(Role.DUTY);

		Mockito.when(userService.findByTelegramId(telegramId)).thenReturn(Optional.of(user));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertTrue(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(userService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnExternalAuthorization() {
		String telegramId = "111111";
		User user = new User();
		user.setRole(Role.USER);

		Mockito.when(userService.findByTelegramId(telegramId)).thenReturn(Optional.of(user));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertTrue(auth.isExternal());
		Assertions.assertFalse(auth.isInternal());
		Assertions.assertFalse(auth.isAdmin());
		Mockito.verify(userService, Mockito.times(1)).findByTelegramId(telegramId);
	}

	@Test
	public void shouldReturnAdminAuthorization() {
		String telegramId = "000000";

		Mockito.when(userService.findByTelegramId(telegramId)).thenReturn(Optional.of(new User()));
		AuthorizationTelegram auth = service.getInfo(telegramId);

		Assertions.assertNotNull(auth);
		Assertions.assertFalse(auth.isUnknown());
		Assertions.assertFalse(auth.isExternal());
		Assertions.assertTrue(auth.isInternal());
		Assertions.assertTrue(auth.isAdmin());
		Mockito.verify(userService, Mockito.times(1)).findByTelegramId(telegramId);

	}

}
