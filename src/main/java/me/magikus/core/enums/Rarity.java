package me.magikus.core.enums;

import me.magikus.core.util.NumberUtils;
import org.bukkit.ChatColor;

public enum Rarity {

    COMMON(ChatColor.WHITE, 1),
    UNCOMMON(ChatColor.GREEN, 2),
    RARE(ChatColor.BLUE, 3),
    EPIC(ChatColor.DARK_PURPLE, 4),
    LEGENDARY(ChatColor.GOLD, 5),
    MYTHIC(ChatColor.LIGHT_PURPLE, 6),
    DIVINE(ChatColor.AQUA, 7),
    SUPREME(ChatColor.DARK_RED, 99),
    SPECIAL(ChatColor.RED, 100),
    VERY_SPECIAL(ChatColor.RED, 101);

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
        if (start == SUPREME || start == SPECIAL || start == VERY_SPECIAL) {
            if (start == SUPREME) {
                return SUPREME;
            }
            if (levels > 0) {
                return VERY_SPECIAL;
            }
            return SPECIAL;
        }
        int newLevel = start.level() + levels;
        newLevel = NumberUtils.clamp(newLevel, 1, 7);
        for (Rarity r : Rarity.values()) {
            if (r.level() == newLevel) {
                return r;
            }
        }
        return Rarity.COMMON;
    }

    public int level() {
        return level;
    }

    public ChatColor color() {
        return color;
    }


}
