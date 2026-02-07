package com.lucky.bot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilMessageSourceBridge {
    public UtilMessageSourceBridge(MessageSource messageSource) {
        Util.setMessageSource(messageSource);
    }
}
