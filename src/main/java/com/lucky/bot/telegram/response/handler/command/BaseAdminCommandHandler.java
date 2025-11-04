package com.lucky.bot.telegram.response.handler.command;

public abstract class BaseAdminCommandHandler extends BaseCommandHandler implements AdminCommandHandler {
    public BaseAdminCommandHandler(Command command) {
        super(command);
    }
}
