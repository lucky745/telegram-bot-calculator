package com.lucky.bot.telegram.response.handler.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseCommandHandler implements CommandHandler {
    private final Command command;

    @Override
    public boolean canHandle(CommandData commandData) {
        return commandData.command().contains(command.name().toLowerCase());
    }
}
