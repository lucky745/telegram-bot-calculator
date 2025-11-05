package com.lucky.bot.telegram.response.handler.callback;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseCallbackHandler implements CallbackHandler {
    private final CallbackType callbackType;

    @Override
    public boolean canHandle(CallbackData callbackData) {
        return callbackData.getCallbackType() == callbackType;
    }
}
