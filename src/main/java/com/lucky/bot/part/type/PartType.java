package com.lucky.bot.part.type;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum PartType {
    WEAPON("ta"),
    GADGET("tb"),
    CHASSIS("tc"),
    WHEEL("td");

    private final String code;

    PartType(final String code) {
        this.code = code;
    }

    public static Optional<PartType> from(String code) {
        for (PartType partType : values()) {
            if (partType.code.equalsIgnoreCase(code)) {
                return Optional.of(partType);
            }
        }
        return Optional.empty();
    }
}
