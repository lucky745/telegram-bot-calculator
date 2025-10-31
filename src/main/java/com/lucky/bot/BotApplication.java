package com.lucky.bot;

import com.lucky.bot.config.BotConfig;
import com.lucky.bot.telegram.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Slf4j
@SpringBootApplication
public class BotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BotApplication.class, args);

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            Bot bot = ctx.getBean(Bot.class);
            bot.onRegister();
            botsApplication.registerBot(ctx.getBean(BotConfig.class).getBotToken(), bot);
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
