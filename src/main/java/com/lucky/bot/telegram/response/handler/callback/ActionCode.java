package com.lucky.bot.telegram.response.handler.callback;

import lombok.Getter;

@Getter
public enum ActionCode {
    ACTION("aa"),
    TYPE("ab"),
    GRADE("ac"),
    LANGUAGE("ad"),
    PART("ae"),
    SPARE_PART("af"),
    LEVEL("ag");

    private final String code;

    ActionCode(final String code) {
        this.code = code;
    }
}
