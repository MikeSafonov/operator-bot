package com.github.mikesafonov.operatorbot.service.Impl;

import org.springframework.stereotype.Service;

import com.github.mikesafonov.operatorbot.repository.ExternalUserRepository;
import com.github.mikesafonov.operatorbot.service.ExternalUserService;

@Service
public class ExternalUserServiceImpl implements ExternalUserService {

	private final ExternalUserRepository userRepository;

	public ExternalUserServiceImpl(ExternalUserRepository userRepository) {
		this.userRepository = userRepository;
	}
}