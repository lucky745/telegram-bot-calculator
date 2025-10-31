package com.lucky.bot.part.type;

import com.lucky.bot.part.stat.Attack;
import com.lucky.bot.part.stat.Health;

import static com.lucky.bot.util.Util.*;

public class Wheel extends BaseType {
    public Wheel(int health, int attack) {
        super(PartType.WHEEL, new Attack(attack), new Health(health), null, null);
    }

    public Wheel(int health) {
        super(PartType.WHEEL, null, new Health(health), null, null);
    }

    @Override
    public String draw(int level) {
        String drawMainStats = formatSingleStat(drawHealth(), level, () -> getHealth(level));
        String drawAttack = getOptionalStat(hasAttack(), drawAttack(), level, () -> getAttack(level));
        return drawMainStats + drawAttack;
    }

    @Override
    public String diff(int newLevel, int baseLevel) {
        String drawHealth = formatStatDiff(drawHealth(), newLevel, baseLevel, this::getHealth);
        String drawAttack = formatOptionalStatDiff(hasAttack(), drawAttack(), newLevel, baseLevel, this::getAttack);
        return drawHealth + drawAttack;
    }
}
