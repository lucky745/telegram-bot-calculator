package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.StatsPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileReader;

import static com.lucky.bot.telegram.response.handler.command.RecoverCommand.RECOVER_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplyToRecover implements AdminFileCommandHandler {
    public static final String RECOVERY_ERROR = "⚠️ Error during recovery process";
    public static final String DOWNLOADING_ERROR = "⚠️ Error downloading backup file";
    public static final String RECOVERY_FAIL = "❌ Failed to recover statistics";
    public static final String RECOVERY_SUCCESS = "✅ Statistics recovered successfully";
    private final StatsPersistenceService persistenceService;

    @Override
    public boolean canHandle(CommandData commandData) {
        if (!commandData.message().hasDocument()) {
            return false;
        }

        String replyToMsg = commandData.message().getReplyToMessage().getText();
        return replyToMsg.equals(RECOVER_MESSAGE);
    }

    @Override
    public Response handle(CommandData commandData) {
        try {
            String fileId = commandData.message().getDocument().getFileId();
            File downloadedFile = persistenceService.downloadFileWithId(fileId);

            try (FileReader reader = new FileReader(downloadedFile)) {
                String backupData = org.apache.commons.io.IOUtils.toString(reader);

                if (persistenceService.recover(backupData)) {
                    return new Response(RECOVERY_SUCCESS);
                } else {
                    return new Response(RECOVERY_FAIL);
                }

            } catch (Exception e) {
                log.error("Could not recover stats from backup", e);
                return new Response(RECOVERY_ERROR);
            } finally {
                if (downloadedFile.exists()) {
                    boolean deleted = downloadedFile.delete();
                    if (!deleted) {
                        log.warn("Could not delete backup file: {}", downloadedFile.getAbsolutePath());
                    }
                }
            }
        } catch (TelegramApiException e) {
            log.error("Error downloading backup file", e);
            return new Response(DOWNLOADING_ERROR);
        }
    }
}
