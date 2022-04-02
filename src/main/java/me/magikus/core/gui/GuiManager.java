package me.magikus.core.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private static final Map<UUID, SkyblockGui> activeGuis = new HashMap<>();

    public static void setInventory(Player p, SkyblockGui inventory) {
        activeGuis.put(p.getUniqueId(), inventory);
        inventory.openFor(p);
    }

    public static SkyblockGui getActiveInventory(UUID id) {
        return activeGuis.get(id);
    }

}
