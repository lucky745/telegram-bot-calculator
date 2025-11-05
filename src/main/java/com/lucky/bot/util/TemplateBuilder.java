package com.lucky.bot.util;

import org.springframework.stereotype.Component;

@Component
public class TemplateBuilder {
    private static final String BORDERS = "➖➖➖➖➖➖➖➖➖➖\n";
    private static final String TITLE_PATTERN = BORDERS + "    %s%n";
    private static final String PROGRESS_PATTERN = "   ⋆ %s: %s ⇪[%s/%d]%n";
    private static final String LEVEL_PATTERN = "   ⋆ %s: %s ⇪[?/?]%n";
    private static final String STAT_PATTERN = "   ⋆ %s: %s%n";

    private String title = "";
    private String line1 = "";
    private String line2 = "";
    private String line3 = "";
    private String footer = "";

    public TemplateBuilder withTitle(String title) {
        this.title = String.format(TITLE_PATTERN, title);
        return this;
    }

    public TemplateBuilder withProgress(String label, String value, String progress, int max) {
        this.line1 = String.format(PROGRESS_PATTERN, label, value, progress, max);
        return this;
    }

    public TemplateBuilder withLevel(String label, String value) {
        this.line1 = String.format(LEVEL_PATTERN, label, value);
        return this;
    }

    public TemplateBuilder withLine2(String label, String value) {
        this.line2 = String.format(STAT_PATTERN, label, value);
        return this;
    }

    public TemplateBuilder withLine3(String label, String value) {
        this.line3 = String.format(STAT_PATTERN, label, value);
        return this;
    }

    public TemplateBuilder withFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String build() {
        StringBuilder content = new StringBuilder();

        if (!title.isEmpty()) content.append(title);

        content.append(BORDERS);

        if (!line1.isEmpty()) content.append(line1);
        if (!line2.isEmpty()) content.append(line2);
        if (!line3.isEmpty()) content.append(line3);

        if (!footer.isEmpty()) content.append(footer);
        content.append(BORDERS);

        return content.toString();
    }
}
