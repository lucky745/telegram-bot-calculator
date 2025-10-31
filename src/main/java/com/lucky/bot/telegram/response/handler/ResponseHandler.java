package com.lucky.bot.telegram.response.handler;

public interface ResponseHandler {
    boolean canRespond(String jsonData);

    void respond();
}
