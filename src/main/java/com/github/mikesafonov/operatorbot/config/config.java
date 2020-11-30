package com.github.mikesafonov.operatorbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
@Configuration
public class config {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
