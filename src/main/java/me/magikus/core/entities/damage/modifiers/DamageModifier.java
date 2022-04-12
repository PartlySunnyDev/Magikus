package me.magikus.core.entities.damage.modifiers;

import org.bukkit.ChatColor;

public enum DamageModifier {

    STRONG("✖", ChatColor.DARK_RED),
    WEAK("✗", ChatColor.YELLOW),
    NORMAL("✘", ChatColor.RED),
    ;

    private final String icon;
    private final ChatColor color;

    DamageModifier(String icon, ChatColor color) {
        this.icon = icon;
        this.color = color;
    }

    public String icon() {
        return icon;
    }

    public ChatColor color() {
        return color;
    }
}
