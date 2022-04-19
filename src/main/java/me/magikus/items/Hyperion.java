package me.magikus.items;

import me.magikus.abilities.rightClick.WitherImpactAbility;
import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Hyperion extends MagikusItem {
    public Hyperion(Player p) {
        super("hyperion", true, ItemType.SWORD, p, true, false);
    }

    public Hyperion() {
        super("hyperion", true, ItemType.SWORD, null);
        this.setCanCastSpells(true);
    }

    @Override
    public Material getDefaultItem() {
        return Material.IRON_SWORD;
    }

    @Override
    public String getDisplayName() {
        return "Hyperion";
    }

    @Override
    public AbilityList getAbilities() {
        return new AbilityList(new WitherImpactAbility(this));
    }

    @Override
    public boolean isEnchanted() {
        return false;
    }

    @Override
    public StatList getStats() {
        return new StatList(
                new Stat(StatType.DAMAGE, 260),
                new Stat(StatType.STRENGTH, 150),
                new Stat(StatType.MAX_MANA, 350),
                new Stat(StatType.ATTACK_EFFICIENCY, 30)
        );
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }
}
