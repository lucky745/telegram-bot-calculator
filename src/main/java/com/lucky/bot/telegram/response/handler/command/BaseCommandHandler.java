package com.lucky.bot.telegram.response.handler.command;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@RequiredArgsConstructor
public abstract class BaseCommandHandler implements UserCommandHandler {
    private final Command command;

    @Override
    public boolean canHandle(CommandData commandData) {
        Message msg = commandData.message();
        if (msg == null || !msg.hasText()) return false;

        String text = msg.getText().trim();
        if (!text.startsWith("/")) return false;

        String cmd = text.split("\\s+")[0];
        return cmd.equalsIgnoreCase("/" + command.name().toLowerCase());
    }
}
