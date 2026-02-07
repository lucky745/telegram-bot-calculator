package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotLifecycle implements SmartLifecycle {

    private final TelegramBotsLongPollingApplication botsApp;
    private final Bot bot;
    private final BotConfig botConfig;

    private volatile boolean running = false;

    @Override
    public void start() {
        if (running) return;

        try {
            bot.onRegister();
            botsApp.registerBot(botConfig.getToken(), bot);
            running = true;
            log.info("Telegram bot registered (name={}, creatorId={})", botConfig.getName(), botConfig.getCreatorId());
        } catch (Exception e) {
            log.error("Failed to register Telegram bot", e);
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    /**
     * Start late, after most beans are ready
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
