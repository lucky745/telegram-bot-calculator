package com.lucky.bot.part.stat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseStat {
    private final int amount;
    private final Stat stat;

    @Override
    public String toString() {
        return stat.getIcon();
    }
}
