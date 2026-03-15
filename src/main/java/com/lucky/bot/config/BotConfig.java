package com.lucky.bot.config;

import lombok.Getter;
import lombok.Setter;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "bot")
public class BotConfig {
    private String token;
    private String name;
    private long creatorId;
    private List<Long> adminIds = new ArrayList<>();

    @Bean
    public TelegramClient telegramClient() {

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .protocols(List.of(Protocol.HTTP_1_1))
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build();

        return new OkHttpTelegramClient(client, token);
    }

    /**
     * Used for processing incoming updates.
     * - non-daemon threads -> keep JVM alive
     * - destroyMethod -> graceful shutdown on Spring stop
     */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService botUpdateExecutor() {
        int corePoolSize = Math.max(1, Runtime.getRuntime().availableProcessors());
        int maxPoolSize = corePoolSize + 1;

        ThreadPoolExecutor exec = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            Thread.ofPlatform()
                .name("bot-update-", 1)
                .factory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        exec.prestartAllCoreThreads();
        return exec;
    }
}
