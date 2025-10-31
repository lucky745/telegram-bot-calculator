package com.lucky.bot.part.type;

import com.lucky.bot.part.stat.Attack;
import com.lucky.bot.part.stat.Energy;
import com.lucky.bot.part.stat.Health;

import static com.lucky.bot.util.Util.*;

public class Weapon extends BaseType {
    public Weapon(int attack, int energy) {
        super(PartType.WEAPON, new Attack(attack), null, new Energy(energy), null);
    }

    public Weapon(int attack, int health, int energy) {
        super(PartType.WEAPON, new Attack(attack), new Health(health), new Energy(energy), null);
    }

    @Override
    public String draw(int level) {
        String drawMainStats = formatDoubleStat(drawAttack(), level, () -> getAttack(level), drawEnergy(), getEnergy());
        String drawHealth = getOptionalStat(hasHealth(), drawHealth(), level, () -> getHealth(level));
        return drawMainStats + drawHealth;
    }

    @Override
    public String diff(int newLevel, int baseLevel) {
        String drawAttack = formatStatDiff(drawAttack(), newLevel, baseLevel, this::getAttack);
        String drawHealth = formatOptionalStatDiff(hasHealth(), drawHealth(), newLevel, baseLevel, this::getHealth);
        return drawAttack + drawHealth;
    }
}
