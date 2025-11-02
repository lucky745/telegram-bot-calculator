package com.lucky.bot.telegram.response;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public record Response(String text, InlineKeyboardMarkup keyboard) {
    public Response(String text) {
        this(text, null);
    }
}
