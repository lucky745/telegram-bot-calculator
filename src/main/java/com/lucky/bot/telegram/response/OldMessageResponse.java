package com.lucky.bot.telegram.response;

import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;

import static com.lucky.bot.util.Util.getLocalizedMessage;

public class OldMessageResponse extends BaseResponseHandler {
    private static final String OLD_MESSAGE = "old_message";

    public OldMessageResponse(CallbackType callbackType) {
        super(callbackType);
    }

    @Override
    public void respond() {
        setText(getLocalizedMessage(OLD_MESSAGE, getCallbackData().getLocale()));
    }
}
