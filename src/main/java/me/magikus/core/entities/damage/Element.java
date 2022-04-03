package me.magikus.core.entities.damage;

import me.magikus.core.stats.StatType;
import org.bukkit.ChatColor;

public enum Element {

    FIRE("water", "ice", "fire", StatType.FIRE_DEFENSE, StatType.FIRE_DAMAGE, ChatColor.RED, "☀"),
    WATER("electric", "fire", "water", StatType.WATER_DEFENSE, StatType.WATER_DAMAGE, ChatColor.BLUE, "☁"),
    ELECTRIC("earth", "water", "electric", StatType.ELECTRIC_DEFENSE, StatType.ELECTRIC_DAMAGE, ChatColor.YELLOW, "⚡"),
    WIND("ice", "earth", "wind", StatType.WIND_DEFENSE, StatType.WIND_DAMAGE, ChatColor.WHITE, "๑"),
    ICE("fire", "wind", "ice", StatType.ICE_DEFENSE, StatType.ICE_DAMAGE, ChatColor.AQUA, "❄"),
    EARTH("wind", "electric", "earth", StatType.EARTH_DEFENSE, StatType.EARTH_DAMAGE, ChatColor.GREEN, "۞"),
    NONE("", "", "none", StatType.DEFENSE, StatType.DAMAGE, ChatColor.RED, "✲"),
    ;
    private final String weakness;
    private final String strength;
    private final String id;
    private final StatType defenseStat;
    private final StatType damageStat;
    private final String icon;
    private final ChatColor color;

    Element(String weakness, String strength, String id, StatType defenseStat, StatType damageStat, ChatColor color, String icon) {
        this.weakness = weakness;
        this.strength = strength;
        this.id = id;
        this.defenseStat = defenseStat;
        this.damageStat = damageStat;
        this.color = color;
        this.icon = icon;
    }

    public String weakness() {
        return weakness;
    }

    public String strength() {
        return strength;
    }

    public Element elementWeakness() {
        return getElement(weakness);
    }

    public Element elementStrength() {
        return getElement(strength);
    }

    public StatType defenseStat() {
        return defenseStat;
    }

    public StatType damageStat() {
        return damageStat;
    }

    public String icon() {
        return icon;
    }

    public ChatColor color() {
        return color;
    }

    public String id() {
        return id;
    }

    public static Element getElement(String id) {
        for (Element e : values()) {
            if (e.id().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
