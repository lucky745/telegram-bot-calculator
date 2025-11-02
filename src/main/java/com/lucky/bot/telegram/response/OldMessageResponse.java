package com.lucky.bot.telegram.response;

import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import org.springframework.stereotype.Component;

import static com.lucky.bot.util.Util.getLocalizedMessage;

@Component
public class OldMessageResponse extends BaseResponseHandler {
    private static final String OLD_MESSAGE = "old_message";

    public OldMessageResponse() {
        super(CallbackType.OLD);
    }

    @Override
    public Response respond(CallbackData callbackData) {
        return new Response(getLocalizedMessage(OLD_MESSAGE, callbackData.getLocale()));
    }
}
