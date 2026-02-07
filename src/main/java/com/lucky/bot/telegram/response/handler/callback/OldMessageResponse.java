package com.lucky.bot.telegram.response.handler.callback;

import com.lucky.bot.telegram.response.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static com.lucky.bot.util.Util.getLocalizedMessage;

@Component
public class OldMessageResponse extends BaseCallbackHandler {
    private static final String OLD_MESSAGE = "old_message";

    public OldMessageResponse() {
        super(CallbackType.OLD);
    }

    @Cacheable(value = "old", key = "#callbackData.getLocale()")
    @Override
    public Response handle(CallbackData callbackData) {
        return new Response(getLocalizedMessage(OLD_MESSAGE, callbackData.getLocale()));
    }
}
