package com.lucky.bot.telegram.response;

import com.lucky.bot.part.Part;
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

import static com.lucky.bot.telegram.response.EditLevelResponse.callbackDataChooseLevel;
import static com.lucky.bot.telegram.response.PartTypeResponse.callbackDataChoosePartGrade;
import static com.lucky.bot.util.Util.*;
import static org.telegram.telegrambots.abilitybots.api.util.AbilityUtils.getLocalizedMessage;

@Component
public class PartResponse extends BaseResponseHandler {
    private static final String BACK_TO_PARTS_SELECTION = "back_to_parts";

    public PartResponse() {
        super(CallbackType.PART);
    }

    @Cacheable(value = "part", key = "#callbackData.locale() + ':' + #callbackData.part().id()")
    @Override
    public Response respond(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        Part part = callbackData.getPart();

        String text = getLevelText(part, locale);
        InlineKeyboardMarkup keyboard = selectLevelMarkup(part, locale);
        return new Response(text, keyboard);
    }

    private static String getLevelText(Part part, String locale) {
        return new TemplateBuilder()
                .withTitle(getLocalizedMessage(part.name(), locale))
                .withLevel(getLocalizedMessage(LVL, locale), QUESTION_MARK)
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage("" + part.type(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), part.grade().getStars())
                .withFooter(part.type().draw(0))
                .build();
    }

    private static InlineKeyboardMarkup selectLevelMarkup(Part part, String locale) {
        return inlineKeyboardMarkup(
                chooseLevelRow(1, 2, 3, part.id(), locale),
                chooseLevelRow(4, 5, 6, part.id(), locale),
                chooseLevelRow(7, 8, 9, part.id(), locale),
                backToLevelRow(BACK_TO_PARTS_SELECTION, part, locale)
        );
    }

    private static InlineKeyboardRow chooseLevelRow(int level1, int level2, int level3, int partId, String locale) {
        return new InlineKeyboardRow(
                chooseLevelButton(level1, partId, locale),
                chooseLevelButton(level2, partId, locale),
                chooseLevelButton(level3, partId, locale)
        );
    }

    private static InlineKeyboardButton chooseLevelButton(int level, int partId, String locale) {
        return level <= (MAX_PART_LEVEL - 1) / 10 ? editLevelButton(level, partId, locale) : levelButton(level, partId, locale);
    }

    private static InlineKeyboardButton editLevelButton(int level, int partId, String locale) {
        return inlineKeyboardButton("" + level, callbackDataEditLevel(level, partId, locale));
    }

    private static InlineKeyboardButton levelButton(int level, int partId, String locale) {
        return inlineKeyboardButton("" + level, callbackDataChooseLevel(level, partId, locale));
    }

    private static InlineKeyboardRow backToLevelRow(String backToPart, Part part, String locale) {
        return new InlineKeyboardRow(backToPartButton(backToPart, part, locale));
    }

    private static InlineKeyboardButton backToPartButton(String backToPart, Part part, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(backToPart, locale),
                callbackDataChoosePartGrade(part.grade(), part.type().getPartType(), locale)
        );
    }

    private static Map<String, String> callbackDataEditLevel(int level, int id, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.EDIT_LEVEL.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.PART.getCode(), String.valueOf(id),
                ActionCode.LEVEL.getCode(), String.valueOf(level)
        );
    }
}
