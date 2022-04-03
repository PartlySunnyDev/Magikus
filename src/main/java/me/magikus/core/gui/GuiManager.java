package me.magikus.core.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private static final Map<UUID, MagikusGui> activeGuis = new HashMap<>();

    public static void setInventory(Player p, MagikusGui inventory) {
        activeGuis.put(p.getUniqueId(), inventory);
        inventory.openFor(p);
    }

    public static MagikusGui getActiveInventory(UUID id) {
        return activeGuis.get(id);
    }

}
