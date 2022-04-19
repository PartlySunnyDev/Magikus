package me.magikus.core.entities.stats;

import me.magikus.core.tools.util.DataUtils;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public enum EntityStatType {
    MAX_HEALTH("mg_max_health"),
    DEFENSE("mg_defense"),
    FIRE_DEFENSE("mg_fire_defense"),
    WATER_DEFENSE("mg_water_defense"),
    WIND_DEFENSE("mg_wind_defense"),
    EARTH_DEFENSE("mg_earth_defense"),
    ICE_DEFENSE("mg_ice_defense"),
    ELECTRIC_DEFENSE("mg_electric_defense"),
    SPEED("mg_speed"),
    DAMAGE("mg_damage"),
    FIRE_DAMAGE("mg_fire_damage"),
    WATER_DAMAGE("mg_water_damage"),
    WIND_DAMAGE("mg_wind_damage"),
    EARTH_DAMAGE("mg_earth_damage"),
    ICE_DAMAGE("mg_ice_damage"),
    ELECTRIC_DAMAGE("mg_electric_damage");

    private final String id;

    EntityStatType(String id) {
        this.id = id;
    }

    public static void setStat(Entity e, EntityStatType s, double v) {
        DataUtils.setData(s.id, v, PersistentDataType.DOUBLE, e);
    }

    public static Double getStat(Entity e, EntityStatType s) {
        return (Double) DataUtils.getData(s.id, PersistentDataType.DOUBLE, e);
    }

    public String id() {
        return id;
    }
}
