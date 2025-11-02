package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import com.lucky.bot.telegram.UsageStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

@Slf4j
@Component
@EnableScheduling
public class DailySchedulerConfig {

    @Autowired
    private Bot bot;

    @Scheduled(cron = "@daily")
    public void sendDailyUsageReport() {
        String report = UsageStats.handleUsageCount(bot.getDb());
        bot.getSilent().sendMd(report, bot.creatorId());
        log.info("Daily usage report generated:\n{}", report);
        UsageStats.resetDailyGrowth(bot.getDb());
    }

    @Scheduled(cron = "@daily")
    public void executeDailyBackup() {
        File backup = null;

        try {
            backup = File.createTempFile("backup_", ".json");
            backup.deleteOnExit();

            try (PrintStream printStream = new PrintStream(backup)) {
                Object backupContent = bot.getDb().backup();
                printStream.print(backupContent);

                bot.getTelegramClient().execute(SendDocument.builder()
                        .document(new InputFile(backup))
                        .chatId(bot.creatorId())
                        .build()
                );

                log.info("Daily backup successful: {} bytes sent", backupContent.toString().length());
            } catch (FileNotFoundException e) {
                log.error("Error while fetching backup", e);
            } catch (TelegramApiException e) {
                log.error("Error while sending document/backup file", e);
            }
        } catch (IOException e) {
            log.error("Error creating temporary backup file", e);
        } finally {
            if (backup != null && backup.exists()) {
                boolean deleted = backup.delete();
                if (!deleted) {
                    log.warn("Could not delete backup file: {}", backup.getAbsolutePath());
                }
            }
        }
    }
}
