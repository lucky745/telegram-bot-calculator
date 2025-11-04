package com.lucky.bot.telegram.handler;

import com.lucky.bot.telegram.response.Response;
import com.lucky.bot.telegram.response.handler.PartCalculatorResponseProcessor;
import com.lucky.bot.telegram.response.handler.callback.CallbackData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements UpdateHandler {
    private final PartCalculatorResponseProcessor processor;
    private final SilentSender sender;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void handle(Update update) {
        Response response = processor.processCallback(new CallbackData(update));
        if (response == null) {
            log.warn("No response found for update:{}", update);
            return;
        }
        sender.executeAsyncCallbackResponse(update, response);
    }
}
