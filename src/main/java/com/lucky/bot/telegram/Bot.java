package com.lucky.bot.telegram;

import com.lucky.bot.telegram.handler.SilentSender;
import com.lucky.bot.telegram.handler.UpdateHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class Bot extends LongPollingAsyncUpdateConsumer {
    private static final String NOTIFICATION = "Bot has been successfully started.";
    private final SilentSender sender;
    private final List<UpdateHandler> handlers;

    @Value("${bot.creator-id}")
    private long creatorId;

    public void onRegister() {
        log.info("{} creatorId: {}", NOTIFICATION, creatorId);
        sender.send(NOTIFICATION, creatorId);
    }

    @Override
    public void consume(Update update) {
        handlers.stream()
                .filter(handler -> handler.canHandle(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }
}
