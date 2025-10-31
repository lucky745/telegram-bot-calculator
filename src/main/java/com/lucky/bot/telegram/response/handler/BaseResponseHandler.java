package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Getter
@Setter
public abstract class BaseResponseHandler implements ResponseHandler {
    private final CallbackType callbackType;
    private CallbackData callbackData;
    private String text;
    private InlineKeyboardMarkup keyboardMarkup;
    private boolean toCalculate;

    protected BaseResponseHandler(CallbackType callbackType) {
        this.callbackType = callbackType;
    }

    @Override
    public boolean canRespond(String jsonData) {
        initialize(jsonData);
        return callbackData.getCallbackType() == callbackType;
    }

    private void initialize(String jsonData) {
        callbackData = new CallbackData(jsonData);
        text = null;
        keyboardMarkup = null;
        toCalculate = false;
    }
}
