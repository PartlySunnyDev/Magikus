package me.magikus.core.items.additions;

import me.magikus.core.stats.StatList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface IStatAddition {

    //CALCULATE THE FINAL STATS, NOT JUST FOR ONE ADDITION (include amount)
    @Nullable
    String getLore(Player player);

    StatList getStats(@Nullable Player player, @Nullable Entity e);

    boolean show();

}
