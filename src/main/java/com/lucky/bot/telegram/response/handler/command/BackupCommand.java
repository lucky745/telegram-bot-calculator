package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.handler.SilentSender;
import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.StatsPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@Slf4j
@Component
public class BackupCommand extends BaseAdminCommandHandler {
    public static final String BACKUP_MESSAGE = "📊 Statistics Backup Created";
    public static final String ERROR_CREATING_BACKUP_FILE = "❌ Error creating backup file";
    public static final String ERROR_SENDING_BACKUP_FILE = "❌ Error sending backup file";
    public static final String UNEXPECTED_ERROR_DURING_BACKUP = "❌ Unexpected error during backup";
    public static final String FILE_NAME = "stats_backup.json";
    public static final String FAILED_TO_CREATE_BACKUP = "❌ Failed to create backup";
    private final StatsPersistenceService persistenceService;
    private final SilentSender sender;

    public BackupCommand(StatsPersistenceService persistenceService, SilentSender sender) {
        super(Command.BACKUP);
        this.persistenceService = persistenceService;
        this.sender = sender;
    }

    @Override
    public Response handle(CommandData commandData) {
        long chatId = commandData.user().getId();
        try {
            File backupFile = new File(FILE_NAME);

            try (PrintStream printStream = new PrintStream(backupFile)) {
                String backupData = persistenceService.backup();
                if (backupData == null) {
                    return new Response(FAILED_TO_CREATE_BACKUP);
                }

                printStream.print(backupData);

                sender.getTelegramClient().execute(SendDocument.builder()
                        .document(new InputFile(backupFile, FILE_NAME))
                        .chatId(chatId)
                        .caption(BACKUP_MESSAGE)
                        .build()
                );

                log.info("Backup file sent to admin: {}", chatId);

            } catch (FileNotFoundException e) {
                log.error("Error creating backup file", e);
                return new Response(ERROR_CREATING_BACKUP_FILE);
            } catch (TelegramApiException e) {
                log.error("Error sending backup file", e);
                return new Response(ERROR_SENDING_BACKUP_FILE);
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
        return null;
    }
}
