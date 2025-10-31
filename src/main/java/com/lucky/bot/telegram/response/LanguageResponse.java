package com.lucky.bot.telegram.response;

import com.lucky.bot.part.type.PartType;
import com.lucky.bot.telegram.response.callback.ActionCode;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import com.lucky.bot.telegram.response.template.TemplateBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Map;

import static com.lucky.bot.telegram.response.callback.CallbackType.MENU;
import static com.lucky.bot.util.Util.*;
import static org.telegram.telegrambots.abilitybots.api.util.AbilityUtils.getLocalizedMessage;

public class LanguageResponse extends BaseResponseHandler {
    private static final String LANGUAGE_RESPONSE_PATTERN = "    %s%n%n%s";
    private static final String BACK_TO_LANGUAGE_SELECTION = "back_to_language";
    private static final String SET_LOCALE = "set_locale";

    public LanguageResponse(CallbackType callbackType) {
        super(callbackType);
    }

    @Override
    public void respond() {
        String locale = getCallbackData().getLocale();

        setText(getTypeText(locale));
        setKeyboardMarkup(selectPartTypeMarkup(locale));
    }

    private static String getTypeText(String locale) {
        String typeInfo = new TemplateBuilder()
                .withLine2(getLocalizedMessage(TYPE, locale), QUESTION_MARK)
                .build();
        return String.format(LANGUAGE_RESPONSE_PATTERN, getLocalizedMessage(SET_LOCALE, locale), typeInfo);
    }

    public static InlineKeyboardMarkup selectPartTypeMarkup(String locale) {
        return inlineKeyboardMarkup(
                partTypeRow(PartType.WEAPON, PartType.CHASSIS, locale),
                partTypeRow(PartType.GADGET, PartType.WHEEL, locale),
                backToMenuRow(BACK_TO_LANGUAGE_SELECTION, locale)
        );
    }

    private static InlineKeyboardRow partTypeRow(PartType type1, PartType type2, String locale) {
        return new InlineKeyboardRow(
                partTypeButton(type1, locale),
                partTypeButton(type2, locale)
        );
    }

    private static InlineKeyboardButton partTypeButton(PartType partType, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(partType.name(), locale),
                callbackDataChoosePartType(partType, locale)
        );
    }

    private static InlineKeyboardRow backToMenuRow(String backToMenu, String locale) {
        return new InlineKeyboardRow(backToMenuButton(backToMenu, locale));
    }

    private static InlineKeyboardButton backToMenuButton(String backToMenu, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(backToMenu, locale),
                callbackDataMenu()
        );
    }

    private static Map<String, String> callbackDataMenu() {
        return Map.of(ActionCode.ACTION.getCode(), MENU.getCode());
    }

    protected static Map<String, String> callbackDataChoosePartType(PartType partType, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.PART_TYPE.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.TYPE.getCode(), partType.getCode()
        );
    }
}
