package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.response.Response;
import org.springframework.stereotype.Component;

import static com.lucky.bot.util.Util.IN_DEV;

@Component
public class UsageCommand extends BaseAdminCommandHandler {
    protected UsageCommand() {
        super(Command.USAGE);
    }

    @Override
    public Response handle(CommandData commandData) {
        //todo db
        return new Response(IN_DEV);
    }
}
