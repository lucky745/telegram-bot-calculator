package com.lucky.bot.part.stat;

import lombok.Getter;

import static com.lucky.bot.util.Util.LEVEL_MULTIPLIERS;
import static com.lucky.bot.util.Util.MAX_PART_LEVEL;

@Getter
public abstract class UpgradeableStat extends BaseStat {
    private final int[] levels = new int[MAX_PART_LEVEL];

    public UpgradeableStat(int amount, Stat stat) {
        super(amount, stat);
        initialize();
    }

    public void initialize() {
        levels[0] = getAmount();
        for (int i = 1; i < levels.length; i++) {
            levels[i] = (int) Math.floor(levels[i - 1] * LEVEL_MULTIPLIERS.get(i - 1));
        }
    }
}
