package com.lucky.bot.telegram;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.processor.PartCalculatorResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.abilitybots.api.toggle.CustomToggle;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.BiConsumer;

import static com.lucky.bot.telegram.response.MenuResponse.chooseLanguageMarkup;
import static com.lucky.bot.util.Util.*;
import static org.telegram.telegrambots.abilitybots.api.objects.Flag.CALLBACK_QUERY;
import static org.telegram.telegrambots.abilitybots.api.objects.Locality.USER;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.CREATOR;
import static org.telegram.telegrambots.abilitybots.api.objects.Privacy.PUBLIC;

@Slf4j
@Component
public class Bot extends AbilityBot {
    public static final String START = "start";
    public static final String COUNT = "count";
    public static final String CALLBACK_STATS = "callback";
    private static final String NOTIFICATION = "Bot has been successfully started.";

    @Value("${bot.creator-id}")
    private long creatorId;

    private static final CustomToggle toggle = new CustomToggle()
            .turnOff("promote")
            .turnOff("demote")
            .turnOff("unban")
            .turnOff("ban");

    private final PartCalculatorResponseProcessor processor;

    public Bot(TelegramClient telegramClient, String botUsername, PartCalculatorResponseProcessor processor) {
        super(telegramClient, botUsername, toggle);
        this.processor = processor;
    }

    @Override
    public void onRegister() {
        super.onRegister();
        notification();
    }

    @Override
    public long creatorId() {
        return creatorId;
    }

    private void notification() {
        log.info("{} creatorId: {}", NOTIFICATION, creatorId);
        this.silent.send(NOTIFICATION, creatorId);
    }

    public Ability start() {
        return Ability.builder()
                .name(START)
                .locality(USER)
                .privacy(PUBLIC)
                .setStatsEnabled(true)
                .action(ctx -> silent.execute(SendMessage.builder()
                        .chatId(ctx.chatId())
                        .text(CONVERSATION_START_MESSAGE)
                        .parseMode(MARKDOWN)
                        .replyMarkup(chooseLanguageMarkup())
                        .build()
                ))
                .build();
    }

    public Ability usageCount() {
        return Ability.builder()
                .name(COUNT)
                .locality(USER)
                .privacy(CREATOR)
                .action(ctx -> silent.sendMd(UsageStats.handleUsageCount(getDb()), creatorId()))
                .build();
    }

    public Reply replyToCallbackQueries() {
        BiConsumer<BaseAbilityBot, Update> action = (bot, upd) -> handleReplyToCallbackQuery(upd);
        return Reply.of(action, CALLBACK_QUERY).enableStats(CALLBACK_STATS);
    }

    private void handleReplyToCallbackQuery(Update upd) {
        CallbackData callbackData = new CallbackData(getCallbackData(upd));

        if (callbackData.getCallbackType() == CallbackType.SPARE) {
            UsageStats.incrementUsage(getDb(), getChatId(upd));
        }

        execute(upd, processor.processResponse(callbackData));
    }

    private void execute(Update upd, Response response) {
        if (response.keyboard() == null) {
            silent.execute(simpleCallbackAnswer(getCallbackQueryId(upd), response.text(), true));
        } else {
            silent.execute(simpleCallbackAnswer(getCallbackQueryId(upd)));
            silent.execute(editMessageText(upd, response));
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

    private static String getCallbackData(Update upd) {
        return upd.getCallbackQuery().getData();
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
