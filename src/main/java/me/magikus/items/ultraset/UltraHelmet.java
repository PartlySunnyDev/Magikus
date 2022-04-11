package me.magikus.items.ultraset;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UltraHelmet extends MagikusItem {
    public UltraHelmet(Player p) {
        super("ultrahelmet", true, ItemType.HELMET, p);
    }

    public UltraHelmet() {
        super("ultrahelmet", true, ItemType.HELMET, null, new String[]{"ultrahelmet", "ultrachestplate", "ultraleggings", "ultraboots"});
    }

    @Override
    public UUID skullId() {
        return UUID.fromString("fce0323d-7f50-4317-9720-5f6b14cf78ea");
    }

    @Override
    public String skullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY1MjQxNjZmN2NlODhhNTM3MTU4NzY2YTFjNTExZTMyMmE5M2E1ZTExZGJmMzBmYTZlODVlNzhkYTg2MWQ4In19fQ==";
    }

    @Override
    public Material getDefaultItem() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public String getDisplayName() {
        return "Ultra Helmet";
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
                new Stat(StatType.CRIT_CHANCE, 20),
                new Stat(StatType.CRIT_DAMAGE, 250),
                new Stat(StatType.ATTACK_SPEED, 20),
                new Stat(StatType.LOOT_BONUS, 10),
                new Stat(StatType.INTELLIGENCE, 100),
                new Stat(StatType.STRENGTH, 320),
                new Stat(StatType.SPEED, 120),
                new Stat(StatType.WATER_DEFENSE, 250),
                new Stat(StatType.FIRE_DEFENSE, 250),
                new Stat(StatType.ELECTRIC_DEFENSE, 250),
                new Stat(StatType.MAX_HEALTH, 830)
        );
    }

    @Override
    public String getDescription() {
        return "A very ultra helmet :)";
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
