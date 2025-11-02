package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.callback.CallbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponseHandler implements ResponseHandler {
    private final CallbackType callbackType;

    protected BaseResponseHandler(CallbackType callbackType) {
        this.callbackType = callbackType;
    }

    @Override
    public boolean canRespond(CallbackType callbackType) {
        return this.callbackType == callbackType;
    }
}
