package com.lucky.bot.telegram.response;

import com.lucky.bot.part.grade.PartGrade;
import com.lucky.bot.part.type.PartType;
import com.lucky.bot.telegram.response.callback.ActionCode;
import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import com.lucky.bot.telegram.response.template.TemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Map;

import static com.lucky.bot.telegram.response.MenuResponse.callbackDataChooseLanguage;
import static com.lucky.bot.util.Util.*;
import static org.telegram.telegrambots.abilitybots.api.util.AbilityUtils.getLocalizedMessage;

@Component
public class PartTypeResponse extends BaseResponseHandler {
    private static final String BACK_TO_PART_TYPE_SELECTION = "back_to_part_type";

    public PartTypeResponse() {
        super(CallbackType.PART_TYPE);
    }

    @Cacheable(value = "partGrade", key = "#callbackData.locale() + ':' + #callbackData.partType()")
    @Override
    public Response respond(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        PartType partType = callbackData.getPartType();

        String text = getGradeText(partType, locale);
        InlineKeyboardMarkup keyboard = selectPartGradeMarkup(partType, locale);
        return new Response(text, keyboard);
    }

    private static String getGradeText(PartType partType, String locale) {
        return new TemplateBuilder()
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage(partType.name(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), QUESTION_MARK)
                .build();
    }

    private static InlineKeyboardMarkup selectPartGradeMarkup(PartType partType, String locale) {
        return inlineKeyboardMarkup(
                partGradeRow(PartGrade.STANDARD, PartGrade.POLISHED, partType, locale),
                partGradeRow(PartGrade.REFINED, PartGrade.SUPERIOR, partType, locale),
                partGradeRow(PartGrade.OUTSTANDING, PartGrade.EXTRAORDINARY, partType, locale),
                backToTypeRow(BACK_TO_PART_TYPE_SELECTION, locale)
        );
    }

    private static InlineKeyboardRow partGradeRow(PartGrade grade1, PartGrade grade2, PartType type, String locale) {
        return new InlineKeyboardRow(
                partGradeButton(grade1, type, locale),
                partGradeButton(grade2, type, locale)
        );
    }

    private static InlineKeyboardButton partGradeButton(PartGrade partGrade, PartType partType, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(partGrade.name(), locale),
                callbackDataChoosePartGrade(partGrade, partType, locale)
        );
    }

    private static InlineKeyboardRow backToTypeRow(String backToType, String locale) {
        return new InlineKeyboardRow(backToTypeButton(backToType, locale));
    }

    private static InlineKeyboardButton backToTypeButton(String backToType, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(backToType, locale),
                callbackDataChooseLanguage(locale)
        );
    }

    protected static Map<String, String> callbackDataChoosePartGrade(PartGrade partGrade, PartType partType, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.PART_GRADE.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.TYPE.getCode(), partType.getCode(),
                ActionCode.GRADE.getCode(), partGrade.getCode()
        );
    }
}
