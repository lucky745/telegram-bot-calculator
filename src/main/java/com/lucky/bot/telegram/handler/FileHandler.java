package com.lucky.bot.telegram.handler;

import com.lucky.bot.config.BotConfig;
import com.lucky.bot.telegram.response.handler.PartCalculatorResponseProcessor;
import com.lucky.bot.telegram.response.handler.command.CommandData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileHandler implements UpdateHandler {
    private final PartCalculatorResponseProcessor processor;
    private final SilentSender sender;
    private final BotConfig botConfig;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        CommandData commandData = new CommandData(message, message.getFrom());
        if (isAdmin(message.getFrom())) {
            sender.send(processor.processAdminFileCommand(commandData).text(), message.getChatId());
        }
    }

    private boolean isAdmin(User user) {
        return botConfig.getAdminIds().contains(user.getId());
    }
}
