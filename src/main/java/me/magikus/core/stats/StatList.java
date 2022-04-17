package me.magikus.core.stats;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.util.EntityUtils;
import me.magikus.core.util.ItemUtils;
import me.magikus.core.util.classes.Pair;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StatList {

    public final Map<StatType, Stat> statList = new HashMap<>();
    public final Map<StatType, StatBonus> bonusList = new HashMap<>();

    public StatList() {
    }

    public StatList(Stat... stats) {
        for (Stat stat : stats) {
            statList.put(stat.type(), stat);
        }
    }

    public StatList(Stat[] stats, StatBonus[] bonuses) {
        for (Stat stat : stats) {
            addStat(stat);
        }
        for (StatBonus stat : bonuses) {
            addBonus(stat);
        }
    }

    public double getStat(StatType type) {
        Stat stat = statList.get(type);
        return stat == null ? 0 : stat.value();
    }

    public double getStatWithBonus(StatType type) {
        Stat stat = statList.get(type);
        StatBonus bonus = bonusList.get(type);
        if (bonus != null && stat != null) {
            stat.setValue(stat.value() * (bonus.bonus() + 1));
        }
        return stat == null ? 0 : stat.value();
    }

    public double getBonus(StatType type) {
        StatBonus stat = bonusList.get(type);
        return stat == null ? 0 : stat.bonus();
    }

    public void addStat(Stat stat) {
        statList.put(stat.type(), stat);
    }

    public void removeStat(StatType type) {
        statList.remove(type);
    }

    public void removeStat(Stat stat) {
        statList.remove(stat.type());
    }

    public void addBonus(StatBonus stat) {
        bonusList.put(stat.t(), stat);
    }

    public void removeBonus(StatType type) {
        bonusList.remove(type);
    }

    public void removeBonus(StatBonus stat) {
        bonusList.remove(stat.t());
    }

    public NBTItem applyStats(NBTItem item) {
        ItemUtils.setItemStats(item, getStatWithBonusList());
        ItemUtils.setItemBonus(item, getBonusList());
        return item;
    }

    public Stat[] getStatWithBonusList() {
        Stat[] returned = new Stat[statList.size()];
        int count = 0;
        for (Stat s : statList.values()) {
            returned[count] = s;
            count++;
        }
        return returned;
    }

    public StatBonus[] getBonusList() {
        StatBonus[] returned = new StatBonus[bonusList.size()];
        int count = 0;
        for (StatBonus s : bonusList.values()) {
            returned[count] = s;
            count++;
        }
        return returned;
    }

    public StatList merge(StatList list) {
        for (StatType t : list.statList.keySet()) {
            statList.merge(t, list.statList.get(t), (oldVal, newVal) -> {
                oldVal.setValue(oldVal.value() + newVal.value());
                return oldVal;
            });
        }
        for (StatType t : list.bonusList.keySet()) {
            bonusList.merge(t, list.bonusList.get(t), (oldVal, newVal) -> {
                oldVal.setBonus(oldVal.bonus() * newVal.bonus());
                return oldVal;
            });
        }
        return this;
    }

    public Pair<Map<Element, Double>, Pair<Double, Boolean>> getFinalDamage(Entity e, boolean ignoreHand, DamageType type) {
        EntityInfo i = EntityManager.getEntity(EntityUtils.getId(e));
        Map<Element, Double> elementalDamages = new HashMap<>();
        double bonus = 1 + getBonus(StatType.DAMAGE);
        double critBonus = 1 + getStatWithBonus(StatType.CRIT_DAMAGE) / 100;
        boolean critical = new Random().nextInt(100) < getStatWithBonus(StatType.CRIT_CHANCE);
        double finalDamage;
        double fireDamage = (getStatWithBonus(StatType.FIRE_DAMAGE));
        double waterDamage = (getStatWithBonus(StatType.WATER_DAMAGE));
        double windDamage = (getStatWithBonus(StatType.WIND_DAMAGE));
        double electricDamage = (getStatWithBonus(StatType.ELECTRIC_DAMAGE));
        double earthDamage = (getStatWithBonus(StatType.EARTH_DAMAGE));
        double iceDamage = (getStatWithBonus(StatType.ICE_DAMAGE));
        double regularDamage = (getStatWithBonus(StatType.DAMAGE));
        if (type == DamageType.PHYSICAL) {
            fireDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            waterDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            windDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            electricDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            earthDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            iceDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
            regularDamage *= (5 + getStatWithBonus(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * bonus;
        } else {
            fireDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            waterDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            windDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            electricDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            earthDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            iceDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
            regularDamage *= (5 + getStatWithBonus(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * bonus;
        }

        if (i == null) {
            double combined = fireDamage + waterDamage + windDamage + electricDamage + earthDamage + iceDamage + regularDamage;
            elementalDamages.put(Element.ELECTRIC, electricDamage);
            elementalDamages.put(Element.FIRE, fireDamage);
            elementalDamages.put(Element.WIND, windDamage);
            elementalDamages.put(Element.ICE, iceDamage);
            elementalDamages.put(Element.EARTH, earthDamage);
            elementalDamages.put(Element.WATER, waterDamage);
            elementalDamages.put(Element.NONE, regularDamage);
            finalDamage = combined;
        } else {
            Element element = i.type();
            Element strength = element.elementStrength();
            Element weakness = element.elementWeakness();
            switch (strength) {
                case ICE -> iceDamage /= 2;
                case WATER -> waterDamage /= 2;
                case WIND -> windDamage /= 2;
                case ELECTRIC -> electricDamage /= 2;
                case FIRE -> fireDamage /= 2;
                case EARTH -> earthDamage /= 2;
            }
            switch (weakness) {
                case ICE -> iceDamage *= 2;
                case WATER -> waterDamage *= 2;
                case WIND -> windDamage *= 2;
                case ELECTRIC -> electricDamage *= 2;
                case FIRE -> fireDamage *= 2;
                case EARTH -> earthDamage *= 2;
            }

            double combined = fireDamage + waterDamage + windDamage + electricDamage + earthDamage + iceDamage + regularDamage;
            elementalDamages.put(Element.ELECTRIC, electricDamage);
            elementalDamages.put(Element.FIRE, fireDamage);
            elementalDamages.put(Element.WIND, windDamage);
            elementalDamages.put(Element.ICE, iceDamage);
            elementalDamages.put(Element.EARTH, earthDamage);
            elementalDamages.put(Element.WATER, waterDamage);
            elementalDamages.put(Element.NONE, regularDamage);
            finalDamage = combined;
        }
        if (finalDamage > 50) {
            finalDamage = finalDamage + (new Random().nextInt((int) Math.floor(finalDamage / 25)) - (int) Math.floor(finalDamage / 50));
        }
        return new Pair<>(elementalDamages, new Pair<>(finalDamage, critical));
    }
}
