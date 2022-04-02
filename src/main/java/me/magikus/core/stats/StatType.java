package me.magikus.core.stats;

import org.bukkit.ChatColor;

public enum StatType {

    DAMAGE_MULTIPLIER("mg_damage_multiplier", "damage_multiplier", ChatColor.GRAY, "X", false, 0, false),
    DAMAGE_REDUCTION("mg_damage_reduction", "damage_reduction", ChatColor.GRAY, "-", false, 0, false),
    MAX_HEALTH("mg_max_health", "Health", ChatColor.RED, "❤", false, 8, true),
    HEALTH("mg_health", "hp", ChatColor.RED, "❤", false, 8, true),
    STRENGTH("mg_strength", "Strength", ChatColor.RED, "❁", false, 2, false),
    DAMAGE("mg_damage", "Damage", ChatColor.RED, "❁", false, 1, false),
    DEFENSE("mg_defense", "Defense", ChatColor.GREEN, "❈", false, 9, true),
    TRUE_DEFENSE("mg_true_defense", "True Defense", ChatColor.WHITE, "❂", false, 10, true),
    SPEED("mg_speed", "Speed", ChatColor.WHITE, "✦", false, 12, true),
    SPEED_CAP("mg_speed_cap", "Speed Cap", ChatColor.WHITE, "✦", false, 0, true),
    INTELLIGENCE("mg_intelligence", "Intelligence", ChatColor.AQUA, "✎", false, 11, true),
    MANA("mg_mana", "Mana", ChatColor.AQUA, "✎", false, 11, true),
    CRIT_CHANCE("mg_crit_chance", "Crit Chance", ChatColor.BLUE, "☣", true, 3, false),
    CRIT_DAMAGE("mg_crit_damage", "Crit Damage", ChatColor.BLUE, "☠", true, 4, false),
    ATTACK_SPEED("mg_attack_speed", "Bonus Attack Speed", ChatColor.YELLOW, "⚔", true, 5, false),
    FEROCITY("mg_ferocity", "Ferocity", ChatColor.RED, "⫽", false, 9, true),
    MAGIC_FIND("mg_magic_find", "Magic Find", ChatColor.AQUA, "✯", false, 6, false),
    PET_LUCK("mg_pet_luck", "Pet Luck", ChatColor.LIGHT_PURPLE, "♣", false, 16, true),
    SEA_CREATURE_CHANCE("mg_sea_creature_chance", "Sea Creature Chance", ChatColor.DARK_AQUA, "α", true, 7, false),
    FISHING_SPEED("mg_fishing_speed", "Fishing Speed", ChatColor.WHITE, "☭", true, 7, false),
    ABILITY_DAMAGE("mg_ability_damage", "Ability Damage", ChatColor.RED, "๑", true, 19, true),
    MINING_SPEED("mg_mining_speed", "Mining Speed", ChatColor.GOLD, "⸕", false, 13, true),
    PRISTINE("mg_pristine", "Pristine", ChatColor.DARK_PURPLE, "✧", false, 15, true),
    MINING_FORTUNE("mg_mining_fortune", "Mining Fortune", ChatColor.GOLD, "☘", false, 14, true),
    FARMING_FORTUNE("mg_farming_fortune", "Farming Fortune", ChatColor.GOLD, "☘", false, 17, true),
    FORAGING_FORTUNE("mg_foraging_fortune", "Foraging Fortune", ChatColor.GOLD, "☘", false, 18, true),
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
