package com.lucky.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "botTaskExecutor")
    public TaskExecutor botTaskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(Math.max(2, Runtime.getRuntime().availableProcessors()));
        exec.setMaxPoolSize(Math.max(4, Runtime.getRuntime().availableProcessors() * 2));
        exec.setQueueCapacity(5000);
        exec.setThreadNamePrefix("bot-async-");
        exec.setWaitForTasksToCompleteOnShutdown(true);
        exec.setAwaitTerminationSeconds(10);
        exec.initialize();
        return exec;
    }
}
