package me.magikus.core.tools.util;

import me.magikus.Magikus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class BukkitUtils {

    public static void scheduleRepeatingCancelTask(Runnable r, long delay, long repeat, long stopAfter) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        JavaPlugin p = JavaPlugin.getPlugin(Magikus.class);
        BukkitTask t = scheduler.runTaskTimer(p, r, delay, repeat);
        scheduler.runTaskLater(p, t::cancel, stopAfter);
    }

}
