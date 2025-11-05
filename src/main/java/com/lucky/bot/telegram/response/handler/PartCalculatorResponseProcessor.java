package com.lucky.bot.telegram.response.handler;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.handler.callback.CallbackData;
import com.lucky.bot.telegram.response.handler.callback.CallbackHandler;
import com.lucky.bot.telegram.response.handler.command.AdminCommandHandler;
import com.lucky.bot.telegram.response.handler.command.AdminFileCommandHandler;
import com.lucky.bot.telegram.response.handler.command.CommandData;
import com.lucky.bot.telegram.response.handler.command.UserCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartCalculatorResponseProcessor {
    private final List<CallbackHandler> callbackHandlers;
    private final List<UserCommandHandler> userCommandHandlers;
    private final List<AdminCommandHandler> adminCommandHandlers;
    private final List<AdminFileCommandHandler> adminFileCommandHandlers;

    public Response processCallback(CallbackData callbackData) {
        return callbackHandlers.stream()
                .filter(handler -> handler.canHandle(callbackData))
                .findFirst()
                .map(handler -> handler.handle(callbackData))
                .orElse(null);
    }

    public Response processUserCommand(CommandData commandData) {
        return userCommandHandlers.stream()
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
                .orElse(processUserCommand(commandData));
    }

    public Response processAdminFileCommand(CommandData commandData) {
        return adminFileCommandHandlers.stream()
                .filter(handler -> handler.canHandle(commandData))
                .findFirst()
                .map(handler -> handler.handle(commandData))
                .orElse(null);
    }
}
