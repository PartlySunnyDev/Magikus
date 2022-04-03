package me.magikus.core.items.name;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.items.additions.ascensions.AscensionManager;
import org.bukkit.ChatColor;

public class NameBuilder {

    private String displayName;
    private String ascensionName;
    private boolean frag;
    private int enhancements;
    private ChatColor rarity;

    public NameBuilder setName(String name) {
        this.displayName = name;
        return this;
    }

    public NameBuilder setAscension(String name) {
        Ascension ascension = AscensionManager.getAscension(name);
        if (ascension != null) {
            this.ascensionName = ascension.displayName();
        }
        return this;
    }

    public NameBuilder setRarity(Rarity rarity) {
        this.rarity = rarity.color();
        return this;
    }

    public String build() {
        return rarity + displayName + " " + getEnhancements();
    }

    private String getEnhancements() {
        if (enhancements < 6) {
            return ChatColor.GOLD + "✚".repeat(enhancements);
        }
        return ChatColor.RED + "✚".repeat(enhancements - 5) + ChatColor.GOLD + "✪".repeat(10 - enhancements);
    }

    public NameBuilder setEnhancements(int enhancements) {
        if (enhancements > 9 || enhancements < 0) {
            return this;
        }
        this.enhancements = enhancements;
        return this;
    }


}
