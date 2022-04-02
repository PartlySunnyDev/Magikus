package me.magikus.core.items.name;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.additions.reforges.Reforge;
import me.magikus.core.items.additions.reforges.ReforgeManager;
import org.bukkit.ChatColor;

public class NameBuilder {

    private String displayName;
    private String reforgeName;
    private boolean frag;
    private int stars;
    private ChatColor rarity;

    public NameBuilder setName(String name) {
        this.displayName = name;
        return this;
    }

    public NameBuilder setReforge(String name) {
        Reforge reforge = ReforgeManager.getReforge(name);
        if (reforge != null) {
            this.reforgeName = reforge.displayName();
        }
        return this;
    }

    public NameBuilder setFragged(boolean fragged) {
        this.frag = fragged;
        return this;
    }

    public NameBuilder setRarity(Rarity rarity) {
        this.rarity = rarity.color();
        return this;
    }

    public String build() {
        return rarity + (frag ? "⚚ " : "") + ((reforgeName == null || reforgeName.equals("")) ? "" : reforgeName + " ") + displayName + " " + getStars();
    }

    private String getStars() {
        if (stars < 6) {
            return ChatColor.GOLD + "✪".repeat(stars);
        }
        return ChatColor.RED + "✪".repeat(stars - 5) + ChatColor.GOLD + "✪".repeat(10 - stars);
    }

    public NameBuilder setStars(int stars) {
        if (stars > 9 || stars < 0) {
            return this;
        }
        this.stars = stars;
        return this;
    }


}
