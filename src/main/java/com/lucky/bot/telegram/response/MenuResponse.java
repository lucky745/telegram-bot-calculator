package com.lucky.bot.telegram.response;

import com.lucky.bot.telegram.response.callback.ActionCode;
import com.lucky.bot.telegram.response.callback.CallbackType;
import com.lucky.bot.telegram.response.handler.BaseResponseHandler;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Map;

import static com.lucky.bot.telegram.response.callback.CallbackType.LANGUAGE;
import static com.lucky.bot.util.Util.*;

public class MenuResponse extends BaseResponseHandler {
    private static final String ENG = "English 🇬🇧";
    private static final String RUS = "Русский 🇷🇺";

    public MenuResponse(CallbackType callbackType) {
        super(callbackType);
    }

    @Override
    public void respond() {
        setText(CONVERSATION_START_MESSAGE);
        setKeyboardMarkup(chooseLanguageMarkup());
    }

    public static InlineKeyboardMarkup chooseLanguageMarkup() {
        return inlineKeyboardMarkup(
                new InlineKeyboardRow(
                        languageButton(ENG, LOCALE_EN),
                        languageButton(RUS, LOCALE_RU)
                )
        );
    }

    private static InlineKeyboardButton languageButton(String language, String locale) {
        return inlineKeyboardButton(
                language,
                callbackDataChooseLanguage(locale)
        );
    }

    protected static Map<String, String> callbackDataChooseLanguage(String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), LANGUAGE.getCode(),
                ActionCode.LANGUAGE.getCode(), locale
        );
    }
}
