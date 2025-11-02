package com.lucky.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.db.DBContext;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lucky.bot.util.Util.NEW_LINE;
import static java.lang.String.format;

@Slf4j
@Component
public final class UsageStats {
    public static final int TOP_LIFETIME_LIMIT = 25;
    public static final int TOP_DAILY_LIMIT = 15;
    public static final String USAGE_COUNT_MAP = "USAGE_COUNT";
    public static final String DAILY_GROWTH_MAP = "DAILY_GROWTH";
    public static final String NO_ACTIVITY_YET = "📭 No activity yet";
    public static final String USER_STATS_PATTERN = "👤[%1$d](tg://user?id=%1$d): %2$d";
    public static final String BOT_USAGE_STATS_PATTERN = """
            📊 Usage Stats - %s

            📈 **Lifetime:**
            🎯 Total uses: %,d
            👥 Unique Users: %,d

            🚀 **Today's Activity:**
            ➕ New Uses: %,d
            👤 Active Users: %,d

            🏅 **All-Time Top Users:**
            %s

            🏆 **Top Users Today:**
            %s
            """;

    public static void incrementUsage(DBContext db, long userId) {
        Map<Long, Integer> lifetimeUsage = db.getMap(USAGE_COUNT_MAP);
        lifetimeUsage.merge(userId, 1, Integer::sum);

        Map<Long, Integer> dailyGrowth = db.getMap(DAILY_GROWTH_MAP);
        dailyGrowth.merge(userId, 1, Integer::sum);
    }

    public static String handleUsageCount(DBContext db) {
        Map<Long, Integer> lifetimeUsage = db.getMap(USAGE_COUNT_MAP);
        Map<Long, Integer> dailyGrowth = db.getMap(DAILY_GROWTH_MAP);

        int totalLifetimeUses = calculateTotalUsage(lifetimeUsage);
        int totalUsers = lifetimeUsage.size();

        int dailyTotalUses = calculateTotalUsage(dailyGrowth);
        int dailyActiveUsers = dailyGrowth.size();

        log.info("Usage stats sent: {} total uses by {} users. {} daily uses by {} users",
                totalLifetimeUses,
                totalUsers,
                dailyTotalUses,
                dailyActiveUsers
        );

        return createUsageMessage(
                totalLifetimeUses,
                totalUsers,
                dailyTotalUses,
                dailyActiveUsers,
                lifetimeUsage,
                dailyGrowth
        );
    }

    public static void resetDailyGrowth(DBContext db) {
        Map<Long, Integer> dailyGrowth = db.getMap(DAILY_GROWTH_MAP);
        dailyGrowth.clear();
        log.info("Daily growth counters reset");
    }

    private static String getTopUsers(Map<Long, Integer> usageMap, int limit) {
        if (usageMap.isEmpty()) {
            return NO_ACTIVITY_YET;
        }

        return usageMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(entry -> format(USER_STATS_PATTERN, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(NEW_LINE));
    }

    private static String createUsageMessage(int lifetimeTotal, int totalUsers, int dailyTotal,
                                             int dailyUsers, Map<Long, Integer> lifetimeUsage,
                                             Map<Long, Integer> dailyGrowth) {
        return String.format(BOT_USAGE_STATS_PATTERN,
                LocalDate.now(),
                lifetimeTotal,
                totalUsers,
                dailyTotal,
                dailyUsers,
                getTopUsers(lifetimeUsage, TOP_LIFETIME_LIMIT),
                getTopUsers(dailyGrowth, TOP_DAILY_LIMIT)
        );
    }

    private static int calculateTotalUsage(Map<Long, Integer> countMap) {
        return countMap.values().stream().mapToInt(Integer::intValue).sum();
    }
}
