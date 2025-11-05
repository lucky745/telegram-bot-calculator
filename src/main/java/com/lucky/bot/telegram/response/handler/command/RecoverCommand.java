package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.response.Response;
import org.springframework.stereotype.Component;

@Component
public class RecoverCommand extends BaseAdminCommandHandler {
    public static final String RECOVER_MESSAGE = "📥 Please reply to this message with your backup file";

    public RecoverCommand() {
        super(Command.RECOVER);
    }

    @Override
    public Response handle(CommandData commandData) {
        return new Response(RECOVER_MESSAGE);
    }
}
