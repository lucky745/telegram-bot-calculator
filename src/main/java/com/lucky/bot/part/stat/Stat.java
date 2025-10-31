package com.lucky.bot.part.stat;

import lombok.Getter;

@Getter
public enum Stat {
    ATTACK("⚔"), ENERGY("⚡"), HEAL("\uD83D\uDC8A"), HEALTH("❤");

    private final String icon;

    Stat(final String icon) {
        this.icon = icon;
    }
}
