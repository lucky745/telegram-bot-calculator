package com.lucky.bot.telegram.handler;

import com.lucky.bot.telegram.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.concurrent.CompletionException;

import static com.lucky.bot.util.Util.HTML;
import static com.lucky.bot.util.Util.MARKDOWN;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class SilentSender {
    private final TelegramClient telegramClient;

    public <T extends Serializable> void execute(BotApiMethod<T> method) {
        try {
            telegramClient.execute(method);
        } catch (TelegramApiException e) {
            if (isIgnorable(e)) {
                log.debug("Ignored Telegram error: {}", e.getMessage());
                return;
            }
            log.error("Could not execute bot API method", e);
        }
    }

    public <T extends Serializable> void executeAsync(BotApiMethod<T> method) {
        try {
            telegramClient.executeAsync(method)
                    .exceptionally(ex -> {
                        if (isIgnorable(ex)) {
                            log.debug("Ignored async Telegram error: {}", unwrap(ex).getMessage());
                        } else {
                            log.error("Async execution failed", unwrap(ex));
                        }
                        return null;
                    });
        } catch (TelegramApiException e) {
            if (isIgnorable(e)) {
                log.debug("Ignored Telegram error async: {}", e.getMessage());
                return;
            }
            log.error("Synchronous execution failed: {}", e.getMessage(), e);
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
        String callbackId = getCallbackQueryId(upd);

        if (response.keyboard() == null) {
            execute(simpleCallbackAnswer(callbackId, response.text(), true));
            return;
        }

        execute(simpleCallbackAnswer(callbackId));
        executeAsync(editMessageText(upd, response));
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
        return AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .showAlert(alert)
                .text(text)
                .build();
    }

    private static boolean isIgnorable(Throwable t) {
        Throwable root = unwrap(t);
        if (!(root instanceof TelegramApiRequestException)) return false;

        String msg = root.getMessage();
        if (msg == null) return false;

        String m = msg.toLowerCase();
        return m.contains("message is not modified")
                || m.contains("query is too old")
                || m.contains("response timeout expired")
                || m.contains("query id is invalid");
    }

    private static Throwable unwrap(Throwable t) {
        if (t instanceof CompletionException ce && ce.getCause() != null) return ce.getCause();
        return t;
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
