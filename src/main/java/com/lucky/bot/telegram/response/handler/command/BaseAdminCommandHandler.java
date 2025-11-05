package com.lucky.bot.telegram.response.handler.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseAdminCommandHandler implements AdminCommandHandler {
    private final Command command;

    @Override
    public boolean canHandle(CommandData commandData) {
        return commandData.message().getText().contains(command.name().toLowerCase());
    }
}
