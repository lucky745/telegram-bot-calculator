package com.lucky.bot.telegram.handler;

import com.lucky.bot.telegram.response.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

import static com.lucky.bot.util.Util.HTML;
import static com.lucky.bot.util.Util.MARKDOWN;

@Slf4j
@Getter
@Component
public class SilentSender {
    private final TelegramClient telegramClient;

    public SilentSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public <T extends Serializable> void execute(BotApiMethod<T> method) {
        try {
            telegramClient.execute(method);
        } catch (TelegramApiException e) {
            log.error("Could not execute bot API method", e);
        }
    }

    public <T extends Serializable> void executeAsync(BotApiMethod<T> method) {
        try {
            telegramClient.executeAsync(method)
                    .exceptionally(ex -> {
                        log.error("Async execution failed: {}", ex.getMessage());
                        return null;
                    });
        } catch (TelegramApiException e) {
            log.error("Synchronous execution failed: {}", e.getMessage());
        }
    }

    public void send(String txt, long chatId) {
        send(txt, null, chatId);
    }

    public void send(String txt, InlineKeyboardMarkup keyboard, long chatId) {
        executeAsync(SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(txt)
                .replyMarkup(keyboard)
                .parseMode(MARKDOWN)
                .build());
    }

    public void executeAsyncCallbackResponse(Update upd, Response response) {
        if (response.keyboard() == null) {
            executeAsync(simpleCallbackAnswer(getCallbackQueryId(upd), response.text(), true));
        } else {
            executeAsync(simpleCallbackAnswer(getCallbackQueryId(upd)));
            executeAsync(editMessageText(upd, response));
        }
    }

    private EditMessageText editMessageText(Update upd, Response response) {
        return EditMessageText.builder()
                .chatId(getChatId(upd))
                .messageId(getMessageId(upd))
                .text(response.text())
                .replyMarkup(response.keyboard())
                .parseMode(HTML)
                .build();
    }

    private AnswerCallbackQuery simpleCallbackAnswer(String callbackQueryId) {
        return simpleCallbackAnswer(callbackQueryId, null, false);
    }

    private AnswerCallbackQuery simpleCallbackAnswer(String callbackQueryId, String text, boolean alert) {
        return AnswerCallbackQuery
                .builder()
                .callbackQueryId(callbackQueryId)
                .showAlert(alert)
                .text(text)
                .build();
    }

    private static long getChatId(Update upd) {
        return upd.getCallbackQuery().getMessage().getChatId();
    }

    private static int getMessageId(Update upd) {
        return upd.getCallbackQuery().getMessage().getMessageId();
    }

    private static String getCallbackQueryId(Update update) {
        return update.getCallbackQuery().getId();
    }
}
