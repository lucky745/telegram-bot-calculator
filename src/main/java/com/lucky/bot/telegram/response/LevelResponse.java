package com.lucky.bot.telegram.response;

import com.lucky.bot.part.Part;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import com.lucky.bot.telegram.response.template.TemplateBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static com.lucky.bot.telegram.response.EditSparePartsResponse.callbackDataEditSpareParts;
import static com.lucky.bot.telegram.response.PartGradeResponse.callbackDataChoosePart;
import static com.lucky.bot.util.Util.*;
import static org.telegram.telegrambots.abilitybots.api.util.AbilityUtils.getLocalizedMessage;

public class LevelResponse extends BaseResponseHandler {
    private static final String LEVEL_ERROR = "level_error";
    private static final String BACK_TO_LEVEL_SELECTION = "back_to_level";

    public LevelResponse(CallbackType callbackType) {
        super(callbackType);
    }

    @Override
    public void respond() {
        String locale = getCallbackData().getLocale();
        Part part = getCallbackData().getPart();
        int level = getCallbackData().getLevel();

        if (level >= part.grade().getUpgrades().size() + 1) {
            setText(getLocalizedMessage(LEVEL_ERROR, locale));
            return;
        }

        setText(getSparePartsText(part, level, locale));
        setKeyboardMarkup(sparePartsMarkup(part, level, locale));
    }

    private static String getSparePartsText(Part part, int level, String locale) {
        return new TemplateBuilder()
                .withTitle(getLocalizedMessage(part.name(), locale))
                .withProgress(getLocalizedMessage(LVL, locale), "" + level, QUESTION_MARK,
                        part.grade().getUpgrades().get(level - 1).parts())
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage("" + part.type(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), part.grade().getStars())
                .withFooter(part.type().draw(level))
                .build();
    }

    private static InlineKeyboardMarkup sparePartsMarkup(Part part, int level, String locale) {
        return inlineKeyboardMarkup(
                chooseSparePartsRow(1, 2, 3, part.id(), level, locale),
                chooseSparePartsRow(4, 5, 6, part.id(), level, locale),
                chooseSparePartsRow(7, 8, 9, part.id(), level, locale),
                new InlineKeyboardRow(backToLevelButton(BACK_TO_LEVEL_SELECTION, part.id(), locale))
        );
    }

    private static InlineKeyboardRow chooseSparePartsRow(int spare1, int spare2, int spare3, int partId, int level, String locale) {
        return new InlineKeyboardRow(
                editSparePartsButton(spare1, partId, level, locale),
                editSparePartsButton(spare2, partId, level, locale),
                editSparePartsButton(spare3, partId, level, locale)
        );
    }

    private static InlineKeyboardButton editSparePartsButton(int spare, int partId, int level, String locale) {
        return inlineKeyboardButton("" + spare, callbackDataEditSpareParts(spare, partId, level, locale));
    }

    private static InlineKeyboardButton backToLevelButton(String text, int partId, String locale) {
        return inlineKeyboardButton(getLocalizedMessage(text, locale), callbackDataChoosePart(partId, locale));
    }
}
