package me.magikus.core.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private static final Map<UUID, MagikusGui> activeGuis = new HashMap<>();
    private static final Map<String, Class<? extends MagikusGui>> guis = new HashMap<>();

    public static void setInventory(Player p, MagikusGui inventory) {
        activeGuis.put(p.getUniqueId(), inventory);
        inventory.openFor(p);
    }

    public static MagikusGui getActiveInventory(UUID id) {
        return activeGuis.get(id);
    }

    @Nullable
    public static MagikusGui createGui(String id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends MagikusGui> aClass = guis.get(id);
        if (aClass == null) {
            return null;
        }
        return aClass.getDeclaredConstructor().newInstance();
    }

    public static void registerGui(String id, Class<? extends MagikusGui> gui) {
        guis.put(id, gui);
    }

    public static void unregisterGui(String id) {
        guis.remove(id);
    }
}
