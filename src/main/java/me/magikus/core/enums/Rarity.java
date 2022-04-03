package me.magikus.core.enums;

import me.magikus.core.util.NumberUtils;
import org.bukkit.ChatColor;

public enum Rarity {

    NORMAL(ChatColor.WHITE, 1),
    UNCOMMON(ChatColor.GREEN, 2),
    RARE(ChatColor.DARK_AQUA, 3),
    EPIC(ChatColor.DARK_PURPLE, 4),
    LEGENDARY(ChatColor.GOLD, 5),
    RENOWNED(ChatColor.DARK_PURPLE, 6),
    HOLY(ChatColor.YELLOW, 7),
    ULTRA(ChatColor.DARK_RED, 99),
    UNIQUE(ChatColor.RED, 100),
    VERY_UNIQUE(ChatColor.RED, 101);

    private final ChatColor color;
    //Lower means worse
    private final int level;

    Rarity(ChatColor color, int level) {
        this.color = color;
        this.level = level;
    }

    public static Rarity add(Rarity start, int levels) {
        if (levels == 0) {
            return start;
        }
        if (start == ULTRA || start == UNIQUE || start == VERY_UNIQUE) {
            if (start == ULTRA) {
                return ULTRA;
            }
            if (levels > 0) {
                return VERY_UNIQUE;
            }
            return UNIQUE;
        }
        int newLevel = start.level() + levels;
        newLevel = NumberUtils.clamp(newLevel, 1, 7);
        for (Rarity r : Rarity.values()) {
            if (r.level() == newLevel) {
                return r;
            }
        }
        return Rarity.NORMAL;
    }

    public int level() {
        return level;
    }

    public ChatColor color() {
        return color;
    }


}
