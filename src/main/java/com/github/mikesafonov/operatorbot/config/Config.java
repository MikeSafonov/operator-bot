package com.github.mikesafonov.operatorbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.time.Clock;
@Configuration
public class Config {
    @Value("${bot.host}")
    private String host;
    @Value("${bot.port}")
    private Integer port;
    @Value("${bot.type}")
    private String type;

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public DefaultBotOptions setProxy() {
        DefaultBotOptions options = new DefaultBotOptions();
        if(host != null && port != null) {
            options.setProxyHost(host);
            options.setProxyPort(port);
            options.setProxyType(DefaultBotOptions.ProxyType.valueOf(type));
        }
        return options;
    }
}
