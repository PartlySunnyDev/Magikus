package me.magikus.core;

import me.magikus.Magikus;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ConsoleLogger {

    private static final Logger log = JavaPlugin.getPlugin(Magikus.class).getLogger();

    public static void console(String msg) {
        log.info(msg);
    }

    public static void console(String[] msg) {
        for (String s : msg) {
            log.info(s);
        }
    }
}
