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

public class UltraLeggings extends MagikusItem {
    public UltraLeggings(Player p) {
        super("ultraleggings", true, ItemType.LEGGINGS, p);
    }

    public UltraLeggings() {
        super("ultraleggings", true, ItemType.LEGGINGS, null, new String[]{"ultrahelmet", "ultrachestplate", "ultraleggings", "ultraboots"});
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(20, 20, 20);
    }

    @Override
    public Material getDefaultItem() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public String getDisplayName() {
        return "Ultra Leggings";
    }

    @Override
    public AbilityList getAbilities() {
        return new AbilityList();
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public StatList getStats() {
        return new StatList(
                new Stat(StatType.CRIT_CHANCE, 30),
                new Stat(StatType.CRIT_DAMAGE, 240),
                new Stat(StatType.ATTACK_SPEED, 15),
                new Stat(StatType.INTELLIGENCE, 350),
                new Stat(StatType.SPEED, 120),
                new Stat(StatType.STRENGTH, 20),
                new Stat(StatType.DEFENSE, 500),
                new Stat(StatType.MAX_HEALTH, 530)
        );
    }

    @Override
    public String getDescription() {
        return "Very ultra leggings :)";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public boolean fraggable() {
        return true;
    }

    @Override
    public boolean enhanceable() {
        return true;
    }
}
