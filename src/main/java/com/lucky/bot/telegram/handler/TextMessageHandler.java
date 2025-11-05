package com.lucky.bot.telegram.handler;

import com.lucky.bot.config.BotConfig;
import com.lucky.bot.telegram.response.Response;
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
public class TextMessageHandler implements UpdateHandler {
    private final PartCalculatorResponseProcessor processor;
    private final SilentSender sender;
    private final BotConfig botConfig;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() &&
                update.getMessage().getChat().isUserChat() &&
                update.getMessage().hasText();
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        CommandData commandData = new CommandData(message, message.getFrom());
        long chatId = message.getChatId();
        boolean isAdmin = isAdmin(message.getFrom());
        Response response = isAdmin
                ? processor.processAdminCommand(commandData)
                : processor.processUserCommand(commandData);
        if (response == null) {
            log.warn("No response found for update:{}", update);
            return;
        }
        sender.send(response.text(), response.keyboard(), chatId);
    }

    private boolean isAdmin(User user) {
        return botConfig.getAdminIds().contains(user.getId());
    }
}
