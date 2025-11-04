package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.Response;

public interface RequestHandler<T> {
    boolean canHandle(T request);

    Response handle(T request);
}
