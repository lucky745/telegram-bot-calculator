package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import com.lucky.bot.util.CalculatorUsageTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetMe;

@Slf4j
@Component
@EnableScheduling
public class SchedulerConfig {

    private final CalculatorUsageTracker usageTracker;
    private final Bot bot;

    public SchedulerConfig(CalculatorUsageTracker usageTracker, Bot bot) {
        this.usageTracker = usageTracker;
        this.bot = bot;
    }

    @Scheduled(fixedRate = 300_000)
    public void healthCheckKeepAlive() {
        try {
            bot.getSender().execute(new GetMe());

            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
            long maxMemory = runtime.maxMemory() / (1024 * 1024);

            log.debug("Bot healthy | Memory: {}MB/{}MB", usedMemory, maxMemory);
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "@daily")
    public void sendDailyUsageReport() {
        String report = usageTracker.getComprehensiveStats();
        bot.getSender().send(report, bot.getCreatorId());
        log.info("Daily usage report generated:\n{}", report);
        usageTracker.resetDailyCounters();
    }
}
