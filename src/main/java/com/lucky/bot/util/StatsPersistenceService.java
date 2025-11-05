package com.lucky.bot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsPersistenceService {
    private static final String BACKUP_FILENAME = "stats_backup.json";
    private final CalculatorUsageTracker usageStats;
    private final TelegramClient telegramClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String backup() {
        try {
            StatsBackup backup = createBackup();
            return objectMapper.writeValueAsString(backup);
        } catch (IOException e) {
            log.error("Failed to create backup", e);
            return null;
        }
    }

    public boolean recover(String backupData) {
        try {
            StatsBackup backup = objectMapper.readValue(backupData, StatsBackup.class);

            restoreFromBackup(backup);

            log.info("Statistics recovered successfully ({} users, {} parts)",
                    backup.userCalculations.size(), backup.partCalculations.size());
            return true;

        } catch (IOException e) {
            log.error("Failed to recover from backup", e);
            return false;
        }
    }

    public File downloadFileWithId(String fileId) throws TelegramApiException {
        return telegramClient.downloadFile(
                telegramClient.execute(GetFile.builder().fileId(fileId).build())
        );
    }

    private StatsBackup createBackup() {
        return new StatsBackup(
                convertAtomicMap(usageStats.getUserCalculations()),
                convertAtomicMap(usageStats.getPartCalculations()),
                usageStats.getTotalCalculations().get()
        );
    }

    private void restoreFromBackup(StatsBackup backup) {
        usageStats.getUserCalculations().clear();
        usageStats.getPartCalculations().clear();

        backup.userCalculations.forEach((userId, count) ->
                usageStats.getUserCalculations().put(userId, new AtomicInteger(count)));

        backup.partCalculations.forEach((partName, count) ->
                usageStats.getPartCalculations().put(partName, new AtomicInteger(count)));

        usageStats.getTotalCalculations().set(backup.totalCalculations);
    }

    private <K> Map<K, Integer> convertAtomicMap(Map<K, AtomicInteger> atomicMap) {
        Map<K, Integer> result = new HashMap<>();
        atomicMap.forEach((k, v) -> result.put(k, v.get()));
        return result;
    }

    public static class StatsBackup {
        public Map<Long, Integer> userCalculations;
        public Map<String, Integer> partCalculations;
        public long totalCalculations;

        public StatsBackup() {
        }

        public StatsBackup(Map<Long, Integer> userCalculations,
                           Map<String, Integer> partCalculations,
                           long totalCalculations) {
            this.userCalculations = userCalculations;
            this.partCalculations = partCalculations;
            this.totalCalculations = totalCalculations;
        }
    }
}
