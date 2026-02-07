package com.lucky.bot.telegram.response.handler.callback;

import com.lucky.bot.part.Part;
import com.lucky.bot.part.grade.PartGrade;
import com.lucky.bot.part.type.PartType;
import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.TemplateBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.lucky.bot.telegram.response.handler.callback.LanguageResponse.callbackDataChoosePartType;
import static com.lucky.bot.util.Util.GRADE;
import static com.lucky.bot.util.Util.QUESTION_MARK;
import static com.lucky.bot.util.Util.TYPE;
import static com.lucky.bot.util.Util.getLocalizedMessage;
import static com.lucky.bot.util.Util.getPartsByTypeAndGrade;
import static com.lucky.bot.util.Util.inlineKeyboardButton;
import static com.lucky.bot.util.Util.inlineKeyboardMarkup;

@Component
public class PartGradeResponse extends BaseCallbackHandler {
    private static final String LIST_EMPTY = "list_empty";
    private static final String BACK_TO_GRADE_SELECTION = "back_to_grades";

    public PartGradeResponse() {
        super(CallbackType.PART_GRADE);
    }

    @Override
    public Response handle(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        PartType partType = callbackData.getPartType();
        PartGrade partGrade = callbackData.getPartGrade();
        List<Part> filteredParts = getPartsByTypeAndGrade(partType, partGrade);

        if (filteredParts.isEmpty()) {
            return new Response(getLocalizedMessage(LIST_EMPTY, locale));
        }

        String text = getPartText(partType, partGrade, locale);
        InlineKeyboardMarkup keyboard = selectPartMarkup(filteredParts, locale);
        return new Response(text, keyboard);
    }

    private static String getPartText(PartType partType, PartGrade partGrade, String locale) {
        return new TemplateBuilder()
                .withTitle(QUESTION_MARK)
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage(partType.name(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), partGrade.getStars())
                .build();
    }

    private static InlineKeyboardMarkup selectPartMarkup(List<Part> filteredParts, String locale) {
        return inlineKeyboardMarkup(Stream.concat(
                        filteredParts.stream().map(part -> partRow(part, locale)),
                        Stream.of(backToGradeRow(BACK_TO_GRADE_SELECTION, filteredParts.getFirst(), locale))
                ).toList()
        );
    }

    private static InlineKeyboardRow partRow(Part part, String locale) {
        return new InlineKeyboardRow(partButton(part, locale));
    }

    private static InlineKeyboardButton partButton(Part part, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(part.name(), locale),
                callbackDataChoosePart(part.id(), locale)
        );
    }

    private static InlineKeyboardRow backToGradeRow(String backToGrade, Part part, String locale) {
        return new InlineKeyboardRow(backToGradeButton(backToGrade, part.type().getPartType(), locale));
    }

    private static InlineKeyboardButton backToGradeButton(String backToGrade, PartType partType, String locale) {
        return inlineKeyboardButton(
                getLocalizedMessage(backToGrade, locale),
                callbackDataChoosePartType(partType, locale)
        );
    }

    protected static Map<String, String> callbackDataChoosePart(int id, String locale) {
        return Map.of(
                ActionCode.ACTION.getCode(), CallbackType.PART.getCode(),
                ActionCode.LANGUAGE.getCode(), locale,
                ActionCode.PART.getCode(), String.valueOf(id)
        );
    }
}
