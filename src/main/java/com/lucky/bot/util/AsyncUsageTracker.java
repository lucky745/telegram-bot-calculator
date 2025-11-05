package com.lucky.bot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncUsageTracker {
    private final CalculatorUsageTracker usageTracker;

    @Async
    public void trackUsageAsync(long userId, String partName, int level) {
        usageTracker.trackCalculations(userId, partName, level);
    }
}
