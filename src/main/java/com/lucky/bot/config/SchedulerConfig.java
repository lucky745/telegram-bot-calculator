package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
import com.lucky.bot.telegram.handler.SilentSender;
import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.CalculatorUsageTracker;
import com.lucky.bot.util.StatsPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lucky.bot.telegram.response.handler.command.BackupCommand.*;

@Slf4j
@Component
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private StatsPersistenceService persistenceService;

    @Autowired
    private CalculatorUsageTracker usageTracker;

    @Autowired
    private SilentSender sender;

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private Bot bot;

    @Scheduled(fixedRate = 300000)
    public void healthCheckKeepAlive() {
        try {
            bot.getSender().execute(new GetMe());

            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
            long maxMemory = runtime.maxMemory() / (1024 * 1024);

            log.info("Bot healthy | Memory: {}MB/{}MB", usedMemory, maxMemory);
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

    @Scheduled(cron = "@daily")
    public void executeDailyBackup() {
        sender.send(backupResponse().text(), bot.getCreatorId());
    }

    private Response backupResponse() {
        try {
            File backupFile = new File(FILE_NAME);

            try (PrintStream printStream = new PrintStream(backupFile)) {
                String backupData = persistenceService.backup();
                if (backupData == null) {
                    return new Response(FAILED_TO_CREATE_BACKUP);
                }

                printStream.print(backupData);

                sendBackupToAdmins(backupFile);

            } catch (FileNotFoundException e) {
                log.error("Error creating backup file", e);
                return new Response(ERROR_CREATING_BACKUP_FILE);
            } finally {
                if (backupFile.exists()) {
                    boolean deleted = backupFile.delete();
                    if (!deleted) {
                        log.warn("Could not delete backup file: {}", backupFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error during backup", e);
            return new Response(UNEXPECTED_ERROR_DURING_BACKUP);
        }
        return new Response("Daily backup completed successfully");
    }

    private void sendBackupToAdmins(File backupFile) {
        String caption = "📊 Daily Backup - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (Long adminId : botConfig.getAdminIds()) {
            try {
                sender.getTelegramClient().execute(SendDocument.builder()
                        .document(new InputFile(backupFile, backupFile.getName()))
                        .chatId(adminId)
                        .caption(caption)
                        .build()
                );
                log.info("Daily backup sent to admin: {}", adminId);

            } catch (TelegramApiException e) {
                log.error("Failed to send daily backup to admin: {}", adminId, e);
            }
        }
    }
}
