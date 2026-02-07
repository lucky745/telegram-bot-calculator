package com.lucky.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class LongPollingAsyncUpdateConsumer implements LongPollingUpdateConsumer {

    @Autowired
    protected ExecutorService botUpdateExecutor;

    private final AtomicInteger maxUpdateIdSeen = new AtomicInteger(-1);

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            int updateId = update.getUpdateId();
            int lastSeen = maxUpdateIdSeen.get();

            if (updateId <= lastSeen) {
                log.debug("Skipping duplicate/replayed updateId={}", updateId);
                continue;
            }
            maxUpdateIdSeen.accumulateAndGet(updateId, Math::max);

            botUpdateExecutor.execute(() -> {
                try {
                    this.consume(update);
                } catch (Exception e) {
                    log.error("Error processing update {}", update, e);
                }
            });
        }
    }

    public abstract void consume(Update update);
}
