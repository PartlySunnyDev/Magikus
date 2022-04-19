package me.magikus.core.player;

import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatManager implements Listener {

    public static final Map<UUID, StatList> playerStats = new HashMap<>();

    public static void setStat(UUID id, StatType type, double value) {
        playerStats.get(id).addStat(new Stat(type, value));
    }

    public static double getStatWithBonus(UUID id, StatType type) {
        return playerStats.get(id).getStatWithBonus(type);
    }


    public static void changeStat(UUID id, StatType type, double value) {
        StatList statList = playerStats.get(id);
        statList.addStat(new Stat(type, statList.getStatWithBonus(type) + value));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        BaseStatManager.initializeStats(event.getPlayer());
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        BaseStatManager.removeStats(event.getPlayer());
    }

}
