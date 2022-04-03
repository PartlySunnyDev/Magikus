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

    public StatList() {
    }

    public StatList(Stat... stats) {
        for (Stat stat : stats) {
            statList.put(stat.type(), stat);
        }
    }

    public double getStat(StatType type) {
        Stat stat = statList.get(type);
        return stat == null ? 0 : stat.value();
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

    public NBTItem applyStats(NBTItem item) {
        return ItemUtils.setItemStats(item, asList());
    }

    public Stat[] asList() {
        Stat[] returned = new Stat[statList.size()];
        int count = 0;
        for (Stat s : statList.values()) {
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
        return this;
    }

    public Pair<Map<Element, Double>, Pair<Double, Boolean>> getFinalDamage(Entity e, boolean ignoreHand, DamageType type) {
        EntityInfo i = EntityManager.getEntity(EntityUtils.getId(e));
        Map<Element, Double> elementalDamages = new HashMap<>();
        double multiplier = getStat(StatType.DAMAGE_MULTIPLIER);
        double critBonus = 1 + getStat(StatType.CRIT_DAMAGE) / 100;
        boolean critical = new Random().nextInt(100) < getStat(StatType.CRIT_CHANCE);
        double finalDamage;
        double fireDamage = (getStat(StatType.FIRE_DAMAGE));
        double waterDamage = (getStat(StatType.WATER_DAMAGE));
        double windDamage = (getStat(StatType.WIND_DAMAGE));
        double electricDamage = (getStat(StatType.ELECTRIC_DAMAGE));
        double earthDamage = (getStat(StatType.EARTH_DAMAGE));
        double iceDamage = (getStat(StatType.ICE_DAMAGE));
        double regularDamage = (getStat(StatType.DAMAGE));
        if (type == DamageType.PHYSICAL) {
            fireDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            waterDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            windDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            electricDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            earthDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            iceDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
            regularDamage *= (1 + getStat(StatType.STRENGTH) / 100) * (critical ? critBonus : 1) * multiplier;
        } else {
            fireDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            waterDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            windDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            electricDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            earthDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            iceDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
            regularDamage *= (1 + getStat(StatType.MAGIC_POWER) / 100) * (critical ? critBonus : 1) * multiplier;
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
