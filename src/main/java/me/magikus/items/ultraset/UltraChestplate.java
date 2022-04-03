package me.magikus.items.ultraset;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UltraChestplate extends MagikusItem {
    public UltraChestplate(Player p) {
        super("ultrachestplate", true, ItemType.CHESTPLATE, p);
    }

    public UltraChestplate() {
        super("ultrachestplate", true, ItemType.CHESTPLATE, null, new String[]{"ultrahelmet", "ultrachestplate", "ultraleggings", "ultraboots"});
    }

    @Override
    public boolean fraggable() {
        return true;
    }

    @Override
    public boolean enhanceable() {
        return true;
    }

    @Override
    public Material getDefaultItem() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public String getDisplayName() {
        return "Ultra Chestplate";
    }

    @Override
    public AbilityList getAbilities() {
        return new AbilityList();
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(10, 10, 10);
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public StatList getStats() {
        return new StatList(
                new Stat(StatType.CRIT_CHANCE, 20),
                new Stat(StatType.CRIT_DAMAGE, 250),
                new Stat(StatType.ATTACK_SPEED, 20),
                new Stat(StatType.LOOT_BONUS, 10),
                new Stat(StatType.INTELLIGENCE, 100),
                new Stat(StatType.STRENGTH, 320),
                new Stat(StatType.SPEED, 120),
                new Stat(StatType.WATER_DEFENSE, 250),
                new Stat(StatType.FIRE_DEFENSE, 250),
                new Stat(StatType.EARTH_DEFENSE, 250),
                new Stat(StatType.MAX_HEALTH, 830)
        );
    }

    @Override
    public String getDescription() {
        return "A very ultra chestplate :)";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }
}
