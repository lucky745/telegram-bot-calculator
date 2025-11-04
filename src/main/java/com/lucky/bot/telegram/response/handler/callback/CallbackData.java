package com.lucky.bot.telegram.response.handler.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lucky.bot.part.Part;
import com.lucky.bot.part.grade.PartGrade;
import com.lucky.bot.part.type.PartType;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

import static com.lucky.bot.telegram.response.handler.callback.ActionCode.GRADE;
import static com.lucky.bot.telegram.response.handler.callback.ActionCode.TYPE;
import static com.lucky.bot.telegram.response.handler.callback.ActionCode.*;
import static com.lucky.bot.util.Util.*;

@Slf4j
@Getter
public class CallbackData {
    private static final String OLD_CALLBACK_DATA = "_";

    private final CallbackType callbackType;
    private final PartGrade partGrade;
    private final PartType partType;
    private final String locale;
    private final Part part;
    private final int level;
    private final int spare;
    private final User user;

    @SneakyThrows
    public CallbackData(Update update) {
        user = update.getCallbackQuery().getFrom();
        String jsonData = update.getCallbackQuery().getData();
        Map<String, String> callbackData;
        if (jsonData.contains(OLD_CALLBACK_DATA)) {
            callbackData = new HashMap<>();
            locale = jsonData.contains(LOCALE_RU) ? LOCALE_RU : LOCALE_EN;
            callbackType = CallbackType.OLD;
        } else {
            callbackData = objectMapper.readValue(jsonData, new TypeReference<>() {
            });
            locale = callbackData.get(LANGUAGE.getCode());
            String action = callbackData.get(ACTION.getCode());
            callbackType = CallbackType.from(action).orElse(null);
        }
        String type = callbackData.get(TYPE.getCode());
        partType = PartType.from(type).orElse(null);
        String grade = callbackData.get(GRADE.getCode());
        partGrade = PartGrade.from(grade).orElse(null);
        String partId = callbackData.get(PART.getCode());
        part = StringUtils.isEmpty(partId) ? null : getPartById(Integer.parseInt(partId));
        String lvl = callbackData.get(LEVEL.getCode());
        level = StringUtils.isEmpty(lvl) ? 0 : Integer.parseInt(lvl);
        String spareParts = callbackData.get(SPARE_PART.getCode());
        spare = StringUtils.isEmpty(spareParts) ? 0 : Integer.parseInt(spareParts);
    }
}
