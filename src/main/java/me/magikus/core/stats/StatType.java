package me.magikus.core.stats;

import org.bukkit.ChatColor;

public enum StatType {

    DAMAGE_MULTIPLIER("mg_damage_multiplier", "damage_multiplier", ChatColor.GRAY, "X", false, 0, false),
    DAMAGE_REDUCTION("mg_damage_reduction", "damage_reduction", ChatColor.GRAY, "-", false, 0, false),
    MAX_HEALTH("mg_max_health", "Health", ChatColor.RED, "❤", false, 8, true),
    HEALTH("mg_health", "hp", ChatColor.RED, "❤", false, 8, true),
    STRENGTH("mg_strength", "Strength", ChatColor.RED, "❁", false, 2, false),
    MAGIC_POWER("mg_magic_power", "Magic Power", ChatColor.AQUA, "✣", false, 2, false),
    FIRE_DAMAGE("mg_fire_damage", "Fire Damage", ChatColor.RED, "☀", false, 1, false),
    DAMAGE("mg_damage", "Damage", ChatColor.RED, "✲", false, 1, false),
    WATER_DAMAGE("mg_water_damage", "Water Damage", ChatColor.BLUE, "☁", false, 1, false),
    EARTH_DAMAGE("mg_earth_damage", "Earth Damage", ChatColor.GREEN, "۞", false, 1, false),
    ELECTRIC_DAMAGE("mg_electric_damage", "Electric Damage", ChatColor.YELLOW, "⚡", false, 1, false),
    WIND_DAMAGE("mg_wind_damage", "Wind Damage", ChatColor.WHITE, "๑", false, 1, false),
    ICE_DAMAGE("mg_ice_damage", "Ice Damage", ChatColor.AQUA, "❄", false, 1, false),
    FIRE_DEFENSE("mg_fire_defense", "Fire Defense", ChatColor.RED, "☀", false, 1, false),
    DEFENSE("mg_defense", "Defense", ChatColor.GREEN, "✤", false, 1, false),
    WATER_DEFENSE("mg_water_defense", "Water Defense", ChatColor.BLUE, "☁", false, 1, false),
    EARTH_DEFENSE("mg_earth_defense", "Earth Defense", ChatColor.GREEN, "۞", false, 1, false),
    ELECTRIC_DEFENSE("mg_electric_defense", "Electric Defense", ChatColor.YELLOW, "⚡", false, 1, false),
    WIND_DEFENSE("mg_wind_defense", "Wind Defense", ChatColor.WHITE, "๑", false, 1, false),
    ICE_DEFENSE("mg_ice_defense", "Ice Defense", ChatColor.AQUA, "❄", false, 1, false),
    TRUE_DEFENSE("mg_true_defense", "True Defense", ChatColor.WHITE, "❂", false, 10, true),
    SPEED("mg_speed", "Speed", ChatColor.WHITE, "✦", false, 12, true),
    SPEED_CAP("mg_speed_cap", "Speed Cap", ChatColor.WHITE, "✦", false, 0, true),
    INTELLIGENCE("mg_intelligence", "Intelligence", ChatColor.AQUA, "✎", false, 11, true),
    MANA("mg_mana", "Mana", ChatColor.AQUA, "✎", false, 11, true),
    CRIT_CHANCE("mg_crit_chance", "Crit Chance", ChatColor.BLUE, "☣", true, 3, false),
    CRIT_DAMAGE("mg_crit_damage", "Crit Damage", ChatColor.BLUE, "☠", true, 4, false),
    ATTACK_SPEED("mg_attack_speed", "Attack Speed", ChatColor.YELLOW, "⚔", true, 5, false),
    ATTACK_EFFICIENCY("mg_attack_efficiency", "Attack Efficiency", ChatColor.RED, "⫽", false, 9, true),
    LOOT_BONUS("mg_loot_bonus", "Loot Bonus", ChatColor.AQUA, "✯", false, 6, false),
    HEALTH_REGEN_SPEED("mg_regen_speed", "Regen Speed", ChatColor.GREEN, "❤", true, 0, true),
    MANA_REGEN_SPEED("mg_mana_speed", "Mana Regen Speed", ChatColor.GREEN, "✎", true, 0, true),
    ;

    private final String id;
    private final String displayName;
    private final ChatColor color;
    private final String symbol;
    private final boolean percent;
    //lower the higher it is on the lore
    private final int level;
    //Green section or red section
    private final boolean isGreen;

    StatType(String id, String displayName, ChatColor color, String symbol, boolean percent, int level, boolean isGreen) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.symbol = symbol;
        this.percent = percent;
        this.level = level;
        this.isGreen = isGreen;
    }

    public boolean isGreen() {
        return isGreen;
    }

    public int level() {
        return level;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public String symbol() {
        return symbol;
    }

    public ChatColor color() {
        return color;
    }

    public boolean percent() {
        return percent;
    }
}
