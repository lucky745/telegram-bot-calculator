package com.lucky.bot.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class CalculatorUsageTracker {
    private static final int TOP_USERS_LIFETIME = 25;
    public static final int TOP_PARTS_LIFETIME = 20;
    private static final int TOP_USERS_DAILY = 15;
    private static final int TOP_PARTS_DAILY = 10;
    private static final int RECENT_USAGE_LIMIT = 20;
    private static final int MAX_HISTORY_SIZE = 1000;
    private static final String NO_ACTIVITY_YET = "📭 No activity yet";
    private static final String USER_STATS_PATTERN = "👤[%1$d](tg://user?id=%1$d): %2$d";

    public record UsageRecord(
            LocalDateTime timestamp,
            long userId,
            String partName,
            int level
    ) {
        public UsageRecord(long userId, String partName, int level) {
            this(LocalDateTime.now(), userId, partName, level);
        }
    }

    private final AtomicLong totalCalculations = new AtomicLong(0);
    private final Map<Long, AtomicInteger> userCalculations = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> partCalculations = new ConcurrentHashMap<>();

    private final AtomicLong dailyCalculations = new AtomicLong(0);
    private final Map<Long, AtomicInteger> dailyUserCalculations = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> dailyPartCalculations = new ConcurrentHashMap<>();

    private final List<UsageRecord> usageHistory = Collections.synchronizedList(new ArrayList<>());


    public void trackCalculations(long userId, String partName, int level) {
        totalCalculations.incrementAndGet();

        userCalculations.compute(userId, (k, v) -> {
            if (v == null) return new AtomicInteger(1);
            v.incrementAndGet();
            return v;
        });

        partCalculations.compute(partName, (k, v) -> {
            if (v == null) return new AtomicInteger(1);
            v.incrementAndGet();
            return v;
        });

        dailyCalculations.incrementAndGet();

        dailyUserCalculations.compute(userId, (k, v) -> {
            if (v == null) return new AtomicInteger(1);
            v.incrementAndGet();
            return v;
        });

        dailyPartCalculations.compute(partName, (k, v) -> {
            if (v == null) return new AtomicInteger(1);
            v.incrementAndGet();
            return v;
        });

        synchronized (usageHistory) {
            if (usageHistory.size() >= MAX_HISTORY_SIZE) {
                usageHistory.removeFirst();
            }
            usageHistory.add(new UsageRecord(userId, partName, level));
        }

        log.debug("Tracked spare parts usage: user={}, part={}, level={}",
                userId, partName, level);
    }

    public String getComprehensiveStats() {
        StringBuilder stats = new StringBuilder("\uD83D\uDCC8 Comprehensive Stats\n\n");

        stats.append("📊 *Lifetime:*\n");
        stats.append(String.format("⚙️ Uses: %,d\n", totalCalculations.get()));
        stats.append(String.format("👥 Users: %,d\n", userCalculations.size()));
        stats.append(String.format("🛠️ Parts: %,d\n\n", partCalculations.size()));

        stats.append("\uD83C\uDFC5 *Top Users:*\n");
        stats.append(getTopUsers(userCalculations, TOP_USERS_LIFETIME));

        stats.append("\n🏆 *Top parts*:\n");
        partCalculations.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()))
                .limit(TOP_PARTS_LIFETIME)
                .forEach(entry -> {
                    stats.append(String.format("• %s: %,d\n", entry.getKey().replace("_", "\\_"), entry.getValue().get()));
                });

        stats.append("\n\n🚀 *Today:*\n");
        stats.append(String.format("➕ Uses: %,d\n", dailyCalculations.get()));
        stats.append(String.format("👤 Users: %,d\n", dailyUserCalculations.size()));
        stats.append(String.format("🛠️ Parts: %,d\n\n", dailyPartCalculations.size()));

        stats.append("\uD83C\uDFC6 *Top Users:*\n");
        stats.append(getTopUsers(dailyUserCalculations, TOP_USERS_DAILY));

        stats.append("\n🔥 *Top parts:*:\n");
        dailyPartCalculations.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()))
                .limit(TOP_PARTS_DAILY)
                .forEach(entry -> {
                    stats.append(String.format("• %s: %,d\n", entry.getKey().replace("_", "\\_"), entry.getValue().get()));
                });

        stats.append("\n⏰ *Recent activity:*\n");
        List<UsageRecord> recent = getRecentUsage(RECENT_USAGE_LIMIT);
        if (recent.isEmpty()) {
            stats.append("No recent calculations\n");
        } else {
            for (int i = 0; i < recent.size(); i++) {
                UsageRecord record = recent.get(i);
                stats.append(String.format("№%d User %d %s L%d %s%n",
                        i + 1, record.userId(), record.partName().replace("_", "\\_"),
                        record.level(),
                        record.timestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                ));
            }
        }

        return stats.toString();
    }

    public List<UsageRecord> getRecentUsage(int limit) {
        synchronized (usageHistory) {
            int start = Math.max(0, usageHistory.size() - limit);
            return new ArrayList<>(usageHistory.subList(start, usageHistory.size()));
        }
    }

    public void resetDailyCounters() {
        dailyCalculations.set(0);
        dailyUserCalculations.clear();
        dailyPartCalculations.clear();
        log.info("All daily counters reset");
    }

    private String getTopUsers(Map<Long, AtomicInteger> usageMap, int limit) {
        return usageMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().get(), a.getValue().get()))
                .limit(limit)
                .map(entry -> String.format(USER_STATS_PATTERN, entry.getKey(), entry.getValue().get()))
                .collect(Collectors.joining("\n"));
    }
}
