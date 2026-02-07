package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramLongPollingConfig {

    private final Bot bot;
    private final BotConfig botConfig;

    @Bean(destroyMethod = "close")
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() {
        return new TelegramBotsLongPollingApplication();
    }

    @PostConstruct
    public void registerBot() throws Exception {
        try (TelegramBotsLongPollingApplication botsApp = telegramBotsLongPollingApplication()) {

            bot.onRegister();
            botsApp.registerBot(botConfig.getToken(), bot);
        }
        log.info("Telegram bot registered: {}", botConfig.getName());
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down Telegram bot...");
    }
}
