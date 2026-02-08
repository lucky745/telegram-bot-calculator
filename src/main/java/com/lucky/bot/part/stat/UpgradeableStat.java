package com.lucky.bot.part.stat;

import lombok.Getter;

import static com.lucky.bot.util.Util.LEVEL_MULTIPLIERS;
import static com.lucky.bot.util.Util.MAX_PART_LEVEL;

@Getter
public abstract class UpgradeableStat extends BaseStat {

    private final int[] levels;

    protected UpgradeableStat(int amount, Stat stat) {
        super(amount, stat);
        this.levels = buildLevels(amount);
    }

    private static int[] buildLevels(int baseAmount) {
        int[] lvls = new int[MAX_PART_LEVEL];
        lvls[0] = baseAmount;
        for (int i = 1; i < lvls.length; i++) {
            lvls[i] = (int) Math.floor(lvls[i - 1] * LEVEL_MULTIPLIERS.get(i - 1));
        }
        return lvls;
    }
}
