package com.lucky.bot.config;

import com.lucky.bot.telegram.Bot;
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
import java.io.PrintStream;

@Slf4j
@Component
@EnableScheduling
public class DailySchedulerConfig {

    @Autowired
    private Bot bot;

    @Scheduled(cron = "@daily")
    public void executeDailyBackup() {
        try {
            File backup = new File("backup.json");

            try (PrintStream printStream = new PrintStream(backup)) {
                printStream.print(bot.getDb().backup());
                bot.getTelegramClient().execute(SendDocument.builder()
                        .document(new InputFile(backup))
                        .chatId(bot.creatorId())
                        .build()
                );
                log.info("backup successful");
            } catch (FileNotFoundException e) {
                log.error("Error while fetching backup", e);
            } catch (TelegramApiException e) {
                log.error("Error while sending document/backup file", e);
            }
        } catch (Exception e) {
            log.error("Failed to execute daily commands: {}", e.getMessage());
        }
    }
}
