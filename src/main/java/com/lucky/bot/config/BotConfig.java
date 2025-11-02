package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import com.lucky.bot.telegram.response.processor.PartCalculatorResponseProcessor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Getter
@Component
@Configuration
public class BotConfig {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botUsername;

    @Bean
    public Bot bot(PartCalculatorResponseProcessor processor) {
        return new Bot(new OkHttpTelegramClient(botToken), botUsername, processor);
    }
}
