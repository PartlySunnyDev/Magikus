package me.magikus.core.entities.stats;

import me.magikus.core.util.DataUtils;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public enum EntityStatType {
    MAX_HEALTH("mg_max_health"),
    DEFENSE("mg_defense"),
    SPEED("mg_speed"),
    DAMAGE("mg_damage");

    private final String id;

    EntityStatType(String id) {
        this.id = id;
    }

    public static void setStat(Entity e, EntityStatType s, double v) {
        DataUtils.setData(s.id, v, PersistentDataType.DOUBLE, e);
    }

    public static Double getStatWithBonus(Entity e, EntityStatType s) {
        return (Double) DataUtils.getData(s.id, PersistentDataType.DOUBLE, e);
    }

    public String id() {
        return id;
    }
}
