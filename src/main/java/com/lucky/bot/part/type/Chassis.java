package com.lucky.bot.part.type;

import com.lucky.bot.part.stat.Energy;
import com.lucky.bot.part.stat.Health;

import static com.lucky.bot.util.Util.formatDoubleStat;
import static com.lucky.bot.util.Util.formatStatDiff;

public class Chassis extends BaseType {
    public Chassis(int health, int energy) {
        super(PartType.CHASSIS, null, new Health(health), new Energy(energy), null);
    }

    @Override
    public String draw(int level) {
        return formatDoubleStat(drawHealth(), level, () -> getHealth(level), drawEnergy(), getEnergy());
    }

    @Override
    public String diff(int newLevel, int baseLevel) {
        return formatStatDiff(drawHealth(), newLevel, baseLevel, this::getHealth);
    }
}
