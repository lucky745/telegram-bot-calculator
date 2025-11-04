package com.lucky.bot.telegram.response.handler.command;

import com.lucky.bot.telegram.response.Response;
import org.springframework.stereotype.Component;

import static com.lucky.bot.telegram.response.handler.callback.MenuResponse.chooseLanguageMarkup;
import static com.lucky.bot.util.Util.CONVERSATION_START_MESSAGE;

@Component
public class StartCommand extends BaseCommandHandler {
    public StartCommand() {
        super(Command.START);
    }

    @Override
    public Response handle(CommandData commandData) {
        return new Response(CONVERSATION_START_MESSAGE, chooseLanguageMarkup());
    }
}
