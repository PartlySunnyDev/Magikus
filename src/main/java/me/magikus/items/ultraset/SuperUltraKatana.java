package me.magikus.items.ultraset;

import me.magikus.abilities.rightClick.SmiteAbility;
import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatBonus;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SuperUltraKatana extends MagikusItem {
    public SuperUltraKatana(Player p) {
        super("superultrakatana", true, ItemType.SWORD, p, true, false);
    }

    public SuperUltraKatana() {
        super("superultrakatana", true, ItemType.SWORD, null);
        this.setCanCastSpells(true);
    }

    @Override
    public Material getDefaultItem() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public String getDisplayName() {
        return "Super Ultra Katana";
    }

    @Override
    public AbilityList getAbilities() {
        return new AbilityList(new SmiteAbility(this));
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public StatList getStats() {
        return new StatList(
                new Stat[]{
                        new Stat(StatType.CRIT_CHANCE, 20),
                        new Stat(StatType.CRIT_DAMAGE, 250),
                        new Stat(StatType.WIND_DAMAGE, 740),
                        new Stat(StatType.EARTH_DAMAGE, 370),
                        new Stat(StatType.FIRE_DAMAGE, 370),
                        new Stat(StatType.LOOT_BONUS, 10),
                        new Stat(StatType.MAX_MANA, 800),
                        new Stat(StatType.STRENGTH, 320),
                        new Stat(StatType.ATTACK_SPEED, 1000)
                },
                new StatBonus[]{
                        new StatBonus(StatType.MAX_MANA, 0.5),
                        new StatBonus(StatType.STRENGTH, 0.5),
                        new StatBonus(StatType.MAX_HEALTH, 0.5)
                }
        );
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RENOWNED;
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
