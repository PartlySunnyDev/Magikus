package me.magikus.core.stats;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStat;
import me.magikus.core.entities.stats.EntityStatList;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.util.EntityUtils;
import me.magikus.core.tools.util.ItemUtils;
import org.bukkit.entity.Entity;

import java.util.Collection;
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
        if (bonus == null) {
            return stat == null ? 0 : stat.value();
        }
        return stat == null ? 0 : stat.value() * (bonus.bonus() + 1);
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

        if (i != null) {
            EntityStatList l = i.stats();
            Collection<EntityStat> entityStats = l.getStatList();
            for (EntityStat es : entityStats) {
                String toString = es.type().toString();
                if (toString.endsWith("_DEFENSE")) {
                    Element element = Element.valueOf(toString.substring(0, toString.length() - 8));
                    double defenseValue = EntityStatType.getStat(e, es.type());
                    if (defenseValue > 0) {
                        switch (element) {
                            case ICE -> iceDamage -= ((defenseValue / (defenseValue + 100)) * iceDamage);
                            case WATER -> waterDamage -= ((defenseValue / (defenseValue + 100)) * waterDamage);
                            case WIND -> windDamage -= ((defenseValue / (defenseValue + 100)) * windDamage);
                            case ELECTRIC -> electricDamage -= ((defenseValue / (defenseValue + 100)) * electricDamage);
                            case FIRE -> fireDamage -= ((defenseValue / (defenseValue + 100)) * fireDamage);
                            case EARTH -> earthDamage -= ((defenseValue / (defenseValue + 100)) * earthDamage);
                        }
                    } else {
                        switch (element) {
                            case ICE -> iceDamage += ((-defenseValue / 200) * iceDamage);
                            case WATER -> waterDamage += ((-defenseValue / 200) * waterDamage);
                            case WIND -> windDamage += ((-defenseValue / 200) * windDamage);
                            case ELECTRIC -> electricDamage += ((-defenseValue / 200) * electricDamage);
                            case FIRE -> fireDamage += ((-defenseValue / 200) * fireDamage);
                            case EARTH -> earthDamage += ((-defenseValue / 200) * earthDamage);
                        }
                    }
                }
                if (toString.equals("DEFENSE")) {
                    double defenseValue = EntityStatType.getStat(e, es.type());
                    if (defenseValue > 0) {
                        iceDamage -= ((defenseValue / (defenseValue + 100)) * iceDamage);
                        waterDamage -= ((defenseValue / (defenseValue + 100)) * waterDamage);
                        windDamage -= ((defenseValue / (defenseValue + 100)) * windDamage);
                        electricDamage -= ((defenseValue / (defenseValue + 100)) * electricDamage);
                        fireDamage -= ((defenseValue / (defenseValue + 100)) * fireDamage);
                        earthDamage -= ((defenseValue / (defenseValue + 100)) * earthDamage);
                    } else {
                        iceDamage += ((-defenseValue / 200) * iceDamage);
                        waterDamage += ((-defenseValue / 200) * waterDamage);
                        windDamage += ((-defenseValue / 200) * windDamage);
                        electricDamage += ((-defenseValue / 200) * electricDamage);
                        fireDamage += ((-defenseValue / 200) * fireDamage);
                        earthDamage += ((-defenseValue / 200) * earthDamage);
                    }
                }
            }

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
        if (finalDamage > 50) {
            finalDamage = finalDamage + (new Random().nextInt((int) Math.floor(finalDamage / 25)) - (int) Math.floor(finalDamage / 50));
        }
        return new Pair<>(elementalDamages, new Pair<>(finalDamage, critical));
    }
}
