package com.lucky.bot.telegram.response;

import com.lucky.bot.part.Part;
import com.lucky.bot.telegram.response.callback.ActionCode;
import com.lucky.bot.telegram.response.callback.CallbackData;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import com.lucky.bot.telegram.response.template.TemplateBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Map;
import java.util.stream.IntStream;

import static com.lucky.bot.telegram.response.EditLevelResponse.callbackDataChooseLevel;
import static com.lucky.bot.util.Util.*;

@Component
public class EditSparePartsResponse extends BaseResponseHandler {
    public EditSparePartsResponse() {
        super(CallbackType.EDIT_SPARE);
    }

    @Override
    public Response respond(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        Part part = callbackData.getPart();
        int level = callbackData.getLevel();
        int spare = callbackData.getSpare();

        String text = getEditSparePartsText(part, level, spare, locale);
        InlineKeyboardMarkup keyboard = editSparePartsMarkup(part, level, spare, locale);
        return new Response(text, keyboard);
    }

    private static String getEditSparePartsText(Part part, int level, int spare, String locale) {
        return new TemplateBuilder()
                .withTitle(getLocalizedMessage(part.name(), locale))
                .withProgress(getLocalizedMessage(LVL, locale), "" + level, spare + QUESTION_MARK,
                        part.grade().getUpgrades().get(level - 1).parts())
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage("" + part.type(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), part.grade().getStars())
                .withFooter(part.type().draw(level))
                .build();
    }

    private static InlineKeyboardMarkup editSparePartsMarkup(Part part, int level, int spareToEdit, String locale) {
        return inlineKeyboardMarkup(
                editSparePartsRow(1, 2, 3, part, level, spareToEdit, locale),
                editSparePartsRow(4, 5, 6, part, level, spareToEdit, locale),
                editSparePartsRow(7, 8, 9, part, level, spareToEdit, locale),
                backToSpareRow(part, level, spareToEdit, locale)
        );
    }

    private static InlineKeyboardRow backToSpareRow(Part part, int level, int spareToEdit, String locale) {
        return new InlineKeyboardRow(
                inlineKeyboardButton("🔙", callbackDataChooseLevel(level, part.id(), locale)),
                chooseSparePartsButton(0, part, level, spareToEdit, locale),
                inlineKeyboardButton("✅", callbackDataSpareParts(spareToEdit, part.id(), level, locale))
        );
    }

    private static InlineKeyboardRow editSparePartsRow(int spare1, int spare2, int spare3, Part part, int level, int spareToEdit, String locale) {
        return new InlineKeyboardRow(
                chooseSparePartsButton(spare1, part, level, spareToEdit, locale),
                chooseSparePartsButton(spare2, part, level, spareToEdit, locale),
                chooseSparePartsButton(spare3, part, level, spareToEdit, locale)
        );
    }

    private static InlineKeyboardButton chooseSparePartsButton(int spare, Part part, int level, int spareToEdit, String locale) {
        return spareToEdit * 10 + spare < missingParts(part, level) ? editSparePartsButton(spare, part.id(), level, spareToEdit, locale)
                : sparePartsButton(spare, part.id(), level, spareToEdit, locale);
    }

    private static int missingParts(Part part, int level) {
        return IntStream.range(level - 1, part.grade().getUpgrades().size())
                .map(levelAfterUpgrade -> part.grade().getUpgrades().get(levelAfterUpgrade).parts())
                .sum();
    }

    private static InlineKeyboardButton editSparePartsButton(int spare, int partId, int level, int spareToEdit, String locale) {
        return inlineKeyboardButton("" + spare, callbackDataEditSpareParts(spareToEdit * 10 + spare, partId, level, locale));
    }

    private static InlineKeyboardButton sparePartsButton(int spare, int partId, int level, int spareToEdit, String locale) {
        return inlineKeyboardButton("" + spare, callbackDataSpareParts(spareToEdit * 10 + spare, partId, level, locale));
    }

    private static Map<String, String> callbackDataSpareParts(int spare, int id, int level, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.SPARE.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.PART.getCode(), String.valueOf(id),
                ActionCode.LEVEL.getCode(), String.valueOf(level),
                ActionCode.SPARE_PART.getCode(), String.valueOf(spare)
        );
    }

    protected static Map<String, String> callbackDataEditSpareParts(int spare, int id, int level, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.EDIT_SPARE.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.PART.getCode(), String.valueOf(id),
                ActionCode.LEVEL.getCode(), String.valueOf(level),
                ActionCode.SPARE_PART.getCode(), String.valueOf(spare)
        );
    }
}
