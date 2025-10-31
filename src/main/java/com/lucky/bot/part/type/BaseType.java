package com.lucky.bot.part.type;

import com.lucky.bot.part.stat.Attack;
import com.lucky.bot.part.stat.Energy;
import com.lucky.bot.part.stat.Heal;
import com.lucky.bot.part.stat.Health;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.lucky.bot.part.stat.Stat.*;

@Getter
@AllArgsConstructor
public abstract class BaseType {
    private final PartType partType;
    private final Attack attack;
    private final Health health;
    private final Energy energy;
    private final Heal heal;

    public abstract String draw(int level);

    public abstract String diff(int newLevel, int baseLevel);

    public boolean hasEnergy() {
        return energy != null;
    }

    public boolean hasAttack() {
        return attack != null;
    }

    public boolean hasHealth() {
        return health != null;
    }

    public boolean hasHeal() {
        return heal != null;
    }

    public String drawEnergy() {
        return ENERGY.getIcon();
    }

    public String drawHealth() {
        return HEALTH.getIcon();
    }

    public String drawAttack() {
        return ATTACK.getIcon();
    }

    public String drawHeal() {
        return HEAL.getIcon();
    }

    public int getEnergy() {
        return energy.getAmount();
    }

    public int getHealth(int level) {
        return health.getLevels()[level - 1];
    }

    public int getAttack(int level) {
        return attack.getLevels()[level - 1];
    }

    public int getHeal(int level) {
        return heal.getLevels()[level - 1];
    }

    @Override
    public String toString() {
        return partType.name();
    }
}
