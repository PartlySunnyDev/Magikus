package me.magikus.core.gui;

import me.magikus.core.gui.components.DecorComponent;
import me.magikus.core.gui.components.GuiComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MagikusGui implements Listener {

    protected final String id;
    protected final int slots;
    protected final String guiName;
    protected final List<GuiComponent> contents;
    private final Inventory inventory;

    protected MagikusGui(String id, int slots, String guiName) {
        this.id = id;
        this.slots = slots;
        this.guiName = guiName;
        this.contents = new ArrayList<>(Collections.nCopies(slots, new DecorComponent(Material.LIGHT_GRAY_STAINED_GLASS_PANE, this)));
        buildGui();
        inventory = Bukkit.createInventory(null, slots, guiName);
        updateInventory();
    }

    public String id() {
        return id;
    }

    public int slots() {
        return slots;
    }

    public String guiName() {
        return guiName;
    }

    public GuiComponent getComponent(int index) {
        return contents.get(index);
    }

    public void setComponent(int index, GuiComponent newComponent) {
        contents.set(index, newComponent);
    }

    public void updateInventory() {
        for (int i = 0; i < slots; i++) {
            inventory.setItem(i, getComponent(i).shownItem());
        }
    }

    public void openFor(Player p) {
        p.openInventory(inventory);
    }

    //Called only at the beginning
    protected abstract void buildGui();

    protected List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        System.out.println(e.getInventory());
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

}
