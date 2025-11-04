package com.lucky.bot.telegram.response.handler.callback;

import com.lucky.bot.part.Part;
import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.util.TemplateBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static com.lucky.bot.telegram.response.handler.callback.LanguageResponse.selectPartTypeMarkup;
import static com.lucky.bot.util.Util.*;

@Component
public class SparePartsResponse extends BaseCallbackHandler {
    private static final String TOKEN = "\uD83D\uDFE1";
    private static final String CALCULATOR = "calculator";
    private static final String UPGRADE_IMPOSSIBLE = "upgrade_impossible";
    private static final String CALCULATED_PARTS_PATTERN = "%s<blockquote expandable>%s</blockquote>%n%s";
    private static final String POSSIBLE_UPGRADE_PATTERN = "   ⋆ %s: %d [%s]%n%s   ⋆ %s: %,d\uD83D\uDCB5 %s%n➖➖➖➖➖➖➖➖➖➖%n";
    private static final String SLASH = "/";

    public SparePartsResponse() {
        super(CallbackType.SPARE);
    }

    @Override
    public Response handle(CallbackData callbackData) {
        String locale = callbackData.getLocale();
        Part part = callbackData.getPart();
        int level = callbackData.getLevel();
        int spare = callbackData.getSpare();

        if (spare < part.grade().getUpgrades().get(level - 1).parts()) {
            return new Response(getLocalizedMessage(UPGRADE_IMPOSSIBLE, locale));
        }

        String text = getCalculatedPartsText(part, level, spare, locale);
        InlineKeyboardMarkup keyboard = selectPartTypeMarkup(locale);
        return new Response(text, keyboard);
    }

    private static String getCalculatedPartsText(Part part, int level, int spare, String locale) {
        String chosenPartInfo = new TemplateBuilder()
                .withTitle(getLocalizedMessage(part.name(), locale))
                .withProgress(getLocalizedMessage(LVL, locale), "" + level, "" + spare,
                        part.grade().getUpgrades().get(level - 1).parts())
                .withLine2(getLocalizedMessage(TYPE, locale), getLocalizedMessage("" + part.type(), locale))
                .withLine3(getLocalizedMessage(GRADE, locale), part.grade().getStars())
                .withFooter(part.type().draw(level))
                .build();
        String upgrades = drawPossibleUpgrades(calculateStatsAfterUpgrade(part, level, spare), part, locale);
        return String.format(CALCULATED_PARTS_PATTERN, chosenPartInfo, upgrades, getLocalizedMessage(CALCULATOR, locale));
    }

    private static String drawPossibleUpgrades(List<PossibleUpgrade> possibleUpgrades, Part part, String locale) {
        StringBuilder sb = new StringBuilder();

        for (PossibleUpgrade upgrade : possibleUpgrades) {
            int level = upgrade.levelAfterUpgrade() + 1;
            String progress = upgrade.levelAfterUpgrade() != part.grade().getUpgrades().size()
                    ? upgrade.remainingParts() + SLASH + part.grade().getUpgrades().get(upgrade.levelAfterUpgrade()).parts()
                    : getLocalizedMessage(MAX, locale);

            String tokens = upgrade.requiredTokens() > 0 ? upgrade.requiredTokens() + TOKEN : "";

            sb.append(String.format(POSSIBLE_UPGRADE_PATTERN,
                    getLocalizedMessage(LVL, locale), level, progress,
                    upgrade.statDiff(),
                    getLocalizedMessage(UP, locale), upgrade.requiredCurrency(), tokens)
            );
        }

        return sb.toString();
    }
}
