package com.lucky.bot.telegram.response.processor;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartCalculatorResponseProcessor {
    private final List<BaseResponseHandler> handlers;

    public Response processResponse(CallbackData callbackData) {
        for (BaseResponseHandler handler : handlers) {
            if (handler.canRespond(callbackData.getCallbackType())) {
                return handler.respond(callbackData);
            }
        }
        return null;
    }
}
