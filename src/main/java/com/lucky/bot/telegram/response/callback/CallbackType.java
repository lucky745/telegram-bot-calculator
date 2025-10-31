package com.lucky.bot.telegram.response.callback;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum CallbackType {
    MENU("ca"),
    LANGUAGE("cb"),
    PART_GRADE("cc"),
    PART_TYPE("cd"),
    PART("ce"),
    LEVEL("cf"),
    EDIT_LEVEL("cg"),
    SPARE("ch"),
    EDIT_SPARE("ci"),
    OLD("cj");

    private final String code;

    CallbackType(final String code) {
        this.code = code;
    }

    public static Optional<CallbackType> from(String code) {
        for (CallbackType callbackType : values()) {
            if (callbackType.code.equalsIgnoreCase(code)) {
                return Optional.of(callbackType);
            }
        }
        return Optional.empty();
    }
}
