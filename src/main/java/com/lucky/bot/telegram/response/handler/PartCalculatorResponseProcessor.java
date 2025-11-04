package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.handler.callback.CallbackData;
import com.lucky.bot.telegram.response.handler.callback.CallbackHandler;
import com.lucky.bot.telegram.response.handler.command.AdminCommandHandler;
import com.lucky.bot.telegram.response.handler.command.CommandData;
import com.lucky.bot.telegram.response.handler.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartCalculatorResponseProcessor {
    private final List<CallbackHandler> callbackHandlers;
    private final List<CommandHandler> commandHandlers;
    private final List<AdminCommandHandler> adminCommandHandlers;

    public Response processCallback(CallbackData callbackData) {
        return callbackHandlers.stream()
                .filter(handler -> handler.canHandle(callbackData))
                .findFirst()
                .map(handler -> handler.handle(callbackData))
                .orElse(null);
    }

    public Response processCommand(CommandData commandData) {
        return commandHandlers.stream()
                .filter(handler -> handler.canHandle(commandData))
                .findFirst()
                .map(handler -> handler.handle(commandData))
                .orElse(null);
    }

    public Response processAdminCommand(CommandData commandData) {
        return adminCommandHandlers.stream()
                .filter(handler -> handler.canHandle(commandData))
                .findFirst()
                .map(handler -> handler.handle(commandData))
                .orElse(null);
    }
}
