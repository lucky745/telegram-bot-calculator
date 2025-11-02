package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;

public interface ResponseHandler {
    boolean canRespond(CallbackType callbackType);

    Response respond(CallbackData callbackData);
}
