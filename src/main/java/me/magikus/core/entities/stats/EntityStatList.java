package me.magikus.core.entities.stats;

import me.magikus.core.ConsoleLogger;
import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.player.PlayerUpdater;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.util.EntityUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityStatList {

    private final Map<String, EntityStat> stats = new HashMap<>();

    public EntityStatList(EntityStat... stats) {
        for (EntityStat s : stats) {
            this.stats.put(s.type().id(), s);
        }
    }

    public void addStat(EntityStat stat) {
        stats.put(stat.type().id(), stat);
    }

    public void removeStat(String stat) {
        stats.remove(stat);
    }

    public double getStat(String stat) {
        if (stats.get(stat) == null) {
            return 0;
        }
        return stats.get(stat).value();
    }

    public void applyStats(Entity e) {
        for (EntityStat s : stats.values()) {
            EntityStatType.setStat(e, s.type(), s.value());
        }
    }

    public Collection<EntityStat> getStatList() {
        return stats.values();
    }

    public double getFinalDamageOn(Player p, Entity e, boolean trueDamage) {
        StatList stats = PlayerUpdater.getStats(p, false, e);
        int rawDamage = 0;
        for (EntityStat s : getStatList()) {
            if (s.type().toString().endsWith("_DAMAGE") || s.type().toString().equals("DAMAGE")) {
                rawDamage += s.value();
            }
        }
        if (trueDamage) {
            return rawDamage;
        }
        double regularDefense = stats.getStatWithBonus(StatType.DEFENSE);
        double reduction = stats.getStatWithBonus(StatType.DAMAGE_REDUCTION);
        if (e != null) {
            EntityInfo i = EntityManager.getEntity(EntityUtils.getId(e));
            if (i != null) {
                EntityStatList l = i.stats();
                Collection<EntityStat> entityStats = l.getStatList();
                for (EntityStat es : entityStats) {
                    String toString = es.type().toString();
                    if (toString.endsWith("_DAMAGE")) {
                        Element el = Element.valueOf(toString.substring(0, toString.length() - 7));
                        double damageValue = es.value();
                        double toCompare = stats.getStatWithBonus(StatType.valueOf(el + "_DEFENSE"));
                        if (toCompare > 0) {
                            rawDamage -= (((reduction / 100) * damageValue) + ((toCompare / (toCompare + 100)) * damageValue));
                        } else {
                            rawDamage += (-((reduction / 100) * damageValue) + ((-toCompare / 200) * damageValue));
                        }
                    }
                }
            }
        }
        double reducedDamage = rawDamage - ((regularDefense / (regularDefense + 100)) * rawDamage);
        if (reducedDamage < 0) {
            return 0;
        }
        return reducedDamage;
    }

}
