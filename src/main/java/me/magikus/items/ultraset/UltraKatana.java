package me.magikus.items.ultraset;

import me.magikus.abilities.rightClick.SmiteAbility;
import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UltraKatana extends MagikusItem {
    public UltraKatana(Player p) {
        super("ultrakatana", true, ItemType.SWORD, p, true, false);
    }

    public UltraKatana() {
        super("ultrakatana", true, ItemType.SWORD, null);
        this.setCanCastSpells(true);
    }

    @Override
    public Material getDefaultItem() {
        return Material.IRON_SWORD;
    }

    @Override
    public String getDisplayName() {
        return "Ultra Katana";
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
                new Stat(StatType.CRIT_CHANCE, 20),
                new Stat(StatType.CRIT_DAMAGE, 250),
                new Stat(StatType.ELECTRIC_DAMAGE, 370),
                new Stat(StatType.LOOT_BONUS, 10),
                new Stat(StatType.MAX_MANA, 800),
                new Stat(StatType.STRENGTH, 320),
                new Stat(StatType.ATTACK_SPEED, 1000));
    }

    @Override
    public String getDescription() {
        return null;
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
