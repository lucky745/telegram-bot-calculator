package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import com.lucky.bot.util.CalculatorUsageTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "@daily")
    public void sendDailyUsageReport() {
        String report = usageTracker.getComprehensiveStats();
        bot.getSender().send(report, bot.getCreatorId());
        log.info("Daily usage report generated:\n{}", report);
        usageTracker.resetDailyCounters();
    }
}
