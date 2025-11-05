package com.lucky.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
public abstract class LongPollingAsyncUpdateConsumer implements LongPollingUpdateConsumer {

    @Autowired
    protected Executor botUpdateExecutor;

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(update -> {
            botUpdateExecutor.execute(() -> {
                try {
                    this.consume(update);
                } catch (Exception e) {
                    log.error("Error processing update {}", update, e);
                }
            });
        });
    }

    public abstract void consume(Update update);
}
