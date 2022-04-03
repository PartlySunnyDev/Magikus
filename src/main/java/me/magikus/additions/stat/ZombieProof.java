package me.magikus.additions.stat;

import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.ModifierType;
import me.magikus.core.items.additions.Addition;
import me.magikus.core.items.additions.AdditionInfo;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.items.additions.IStatAddition;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ZombieProof extends Addition implements IStatAddition {

    public ZombieProof() {
        this(null);
    }

    public ZombieProof(MagikusItem parent) {
        super(new AdditionInfo("zombieproof", 1, ModifierType.STAT, ZombieProof.class, AppliableTypeDefaults.armor), parent);
    }

    @Nullable
    @Override
    public String getLore(Player player) {
        return "Take 25% less damage from @@zombies@@";
    }

    @Override
    public StatList getStats(@Nullable Player player, @Nullable Entity e) {
        if (e == null) {
            return new StatList();
        }
        if (e.getType() == EntityType.ZOMBIE || e.getType() == EntityType.ZOMBIE_VILLAGER || e.getType() == EntityType.HUSK) {
            return new StatList(new Stat(StatType.DAMAGE_REDUCTION, 25));
        }
        return new StatList();
    }

    @Override
    public boolean show() {
        return true;
    }
}
