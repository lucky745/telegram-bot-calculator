package com.lucky.bot.part.type;

import com.lucky.bot.part.stat.Energy;
import com.lucky.bot.part.stat.Heal;
import com.lucky.bot.part.stat.Health;

import static com.lucky.bot.util.Util.*;

public class Gadget extends BaseType {
    public Gadget(int health, int energy) {
        super(PartType.GADGET, null, new Health(health), new Energy(energy), null);
    }

    public Gadget(int health, int energy, int heal) {
        super(PartType.GADGET, null, new Health(health), new Energy(energy), new Heal(heal));
    }

    @Override
    public String draw(int level) {
        String drawMainStats = formatDoubleStat(drawHealth(), level, () -> getHealth(level), drawEnergy(), getEnergy());
        String drawHeal = getOptionalStat(hasHeal(), drawHeal(), level, () -> getHeal(level));
        return drawMainStats + drawHeal;
    }

    @Override
    public String diff(int newLevel, int baseLevel) {
        String drawHealth = formatStatDiff(drawHealth(), newLevel, baseLevel, this::getHealth);
        String drawHeal = formatOptionalStatDiff(hasHeal(), drawHeal(), newLevel, baseLevel, this::getHeal);
        return drawHealth + drawHeal;
    }
}
