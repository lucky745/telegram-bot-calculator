package com.lucky.bot.part.grade;

public record Upgrade(int parts, int currency, int tokens) {
    public Upgrade(int parts, int currency) {
        this(parts, currency, 0);
    }
}
