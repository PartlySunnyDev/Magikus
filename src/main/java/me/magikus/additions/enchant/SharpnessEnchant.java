package me.magikus.additions.enchant;

import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.items.additions.enchants.Enchant;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatBonus;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SharpnessEnchant extends Enchant {

    public SharpnessEnchant() {
        this(null, 1);
    }

    public SharpnessEnchant(MagikusItem parent, Integer level) {
        super("sharpness", "Sharpness", 7, level, SharpnessEnchant.class, parent, false, ChatColor.DARK_GREEN, AppliableTypeDefaults.meleeWeapons);
    }

    @Override
    public String description() {
        return "Increases melee damage dealt by %%" + (level() * 5) + "%";
    }

    @Override
    public StatList getBonusStats(@Nullable Player player, @Nullable Entity target) {
        return new StatList(new Stat[] {}, new StatBonus[] {
                new StatBonus(StatType.DAMAGE, (level() * 5) / 100f)
        });
    }
}
