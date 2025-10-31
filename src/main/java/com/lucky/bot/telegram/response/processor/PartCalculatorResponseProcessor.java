package com.lucky.bot.telegram.response.processor;

import com.lucky.bot.telegram.response.*;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PartCalculatorResponseProcessor {
    private final List<BaseResponseHandler> handlers;

    public PartCalculatorResponseProcessor() {
        handlers = Arrays.asList(
                new OldMessageResponse(CallbackType.OLD),
                new MenuResponse(CallbackType.MENU),
                new LanguageResponse(CallbackType.LANGUAGE),
                new PartTypeResponse(CallbackType.PART_TYPE),
                new PartGradeResponse(CallbackType.PART_GRADE),
                new PartResponse(CallbackType.PART),
                new EditLevelResponse(CallbackType.EDIT_LEVEL),
                new LevelResponse(CallbackType.LEVEL),
                new EditSparePartsResponse(CallbackType.EDIT_SPARE),
                new SparePartsResponse(CallbackType.SPARE)
        );
    }

    public BaseResponseHandler processResponse(String jsonData) {
        for (BaseResponseHandler handler : handlers) {
            if (handler.canRespond(jsonData)) {
                handler.respond();
                return handler;
            }
        }
        return null;
    }
}
