package me.magikus.core.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private static final Map<String, GuiInstance> guis = new HashMap<>();

    public static void setInventory(Player p, String id) {
        GuiInstance guiInstance = guis.get(id);
        if (guiInstance == null) {
            return;
        }
        guiInstance.openFor(p);
    }

    public static void registerGui(String id, GuiInstance gui) {
        guis.put(id, gui);
    }

    public static void unregisterGui(String id) {
        guis.remove(id);
    }
}
