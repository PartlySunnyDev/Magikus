package me.magikus.additions.stat;

import me.magikus.core.enums.BracketType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.ModifierType;
import me.magikus.core.items.additions.Addition;
import me.magikus.core.items.additions.AdditionInfo;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.items.additions.IStatAddition;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Infusion extends Addition implements IStatAddition {

    public Infusion() {
        this(null);
    }

    public Infusion(MagikusItem parent) {
        super(new AdditionInfo("infusion", 1, ModifierType.STAT, Infusion.class, 1, ChatColor.GOLD, BracketType.SQUARE, AppliableTypeDefaults.armor), parent);
    }

    @Nullable
    @Override
    public String getLore(Player player) {
        return "Grants you Crit Damage and Strength based on your %%health%%! Also increases your Speed Cap if above @@50%@@ Health";
    }

    @Override
    public StatList getStats(Player player, Entity e) {
        double health;
        if (player == null) {
            health = 100;
        } else {
            health = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.HEALTH);
        }
        StatList statList = new StatList(
                new Stat(StatType.CRIT_DAMAGE, health / 2),
                new Stat(StatType.STRENGTH, health / 2)
        );
        if (player != null) {
            if (health > PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.MAX_HEALTH) / 2) {
                statList.addStat(new Stat(StatType.SPEED_CAP, 100));
            }
        }
        return statList;
    }

    @Override
    public boolean show() {
        return false;
    }
}
