package com.lucky.bot.telegram.response.handler.callback;

import com.lucky.bot.part.Part;
import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.TemplateBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Map;

import static com.lucky.bot.telegram.response.handler.callback.PartGradeResponse.callbackDataChoosePart;
import static com.lucky.bot.util.Util.*;

@Component
public class EditLevelResponse extends BaseCallbackHandler {
    public EditLevelResponse() {
        super(CallbackType.EDIT_LEVEL);
    }

    @Override
    public Response handle(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        Part part = callbackData.getPart();
        int level = callbackData.getLevel();

        String text = getEditLevelText(part, level, locale);
        InlineKeyboardMarkup keyboard = editLevelMarkup(part, level, locale);
        return new Response(text, keyboard);
    }

    private static String getEditLevelText(Part part, int level, String locale) {
        return new TemplateBuilder()
                .withTitle(getLocalizedMessage(part.name(), locale))
                .withLevel(getLocalizedMessage(LVL, locale), level + QUESTION_MARK)
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage("" + part.type(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), part.grade().getStars())
                .withFooter(part.type().draw(level))
                .build();
    }

    private static InlineKeyboardMarkup editLevelMarkup(Part part, int levelToEdit, String locale) {
        return inlineKeyboardMarkup(
                editLevelRow(1, 2, 3, part.id(), levelToEdit, locale),
                editLevelRow(4, 5, 6, part.id(), levelToEdit, locale),
                editLevelRow(7, 8, 9, part.id(), levelToEdit, locale),
                backToLevelRow(part, levelToEdit, locale)
        );
    }

    private static InlineKeyboardRow editLevelRow(int level1, int level2, int level3, int partId, int levelToEdit, String locale) {
        return new InlineKeyboardRow(
                editLevelButton(level1, partId, levelToEdit, locale),
                editLevelButton(level2, partId, levelToEdit, locale),
                editLevelButton(level3, partId, levelToEdit, locale)
        );
    }

    private static InlineKeyboardButton editLevelButton(int level, int partId, int lvlToEdit, String locale) {
        return inlineKeyboardButton("" + level, callbackDataChooseLevel(lvlToEdit * 10 + level, partId, locale));
    }

    private static InlineKeyboardRow backToLevelRow(Part part, int levelToEdit, String locale) {
        return new InlineKeyboardRow(
                inlineKeyboardButton("🔙", callbackDataChoosePart(part.id(), locale)),
                editLevelButton(0, part.id(), levelToEdit, locale),
                inlineKeyboardButton("✅", callbackDataChooseLevel(levelToEdit, part.id(), locale))
        );
    }

    protected static Map<String, String> callbackDataChooseLevel(int level, int id, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.LEVEL.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.PART.getCode(), String.valueOf(id),
                ActionCode.LEVEL.getCode(), String.valueOf(level)
        );
    }
}
