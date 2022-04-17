package me.magikus.core.gui;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.gui.components.GuiComponent;
import me.magikus.core.gui.components.StaticComponent;
import me.magikus.core.gui.components.StorageComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MagikusGui implements Listener {

    protected final String id;
    protected final int slots;
    protected final String guiName;
    private final List<GuiComponent> contents;
    private final Inventory inventory;

    protected MagikusGui(String id, int slots, String guiName) {
        this.id = id;
        this.slots = slots;
        this.guiName = guiName;
        this.contents = new ArrayList<>(Collections.nCopies(slots, new StaticComponent(Material.LIGHT_GRAY_STAINED_GLASS_PANE, this)));
        inventory = Bukkit.createInventory(null, slots, ChatColor.COLOR_CHAR + "x" + guiName);
        buildGui();
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
        if (newComponent instanceof StorageComponent) {
            ((StorageComponent) newComponent).setSlot(index);
        }
        contents.set(index, newComponent);
    }

    public void updateInventory() {
        if (inventory == null || contents == null) {
            return;
        }
        for (int i = 0; i < slots; i++) {
            ItemStack itemStack = getComponent(i).shownItem();
            if (itemStack == null) {
                inventory.setItem(i, new StaticComponent(Material.LIGHT_GRAY_STAINED_GLASS_PANE, this).shownItem());
                continue;
            }
            NBTItem nbti = new NBTItem(itemStack);
            boolean dont = false;
            if ((getComponent(i) instanceof StorageComponent)) {
                if (((StorageComponent) getComponent(i)).empty()) {
                    getComponent(i).setShownItem(((StorageComponent) getComponent(i)).emptyItem());
                } else {
                    dont = true;
                }
            }
            if (!dont) {
                nbti.setBoolean("isgui", true);
                nbti.applyNBT(itemStack);
            }
            inventory.setItem(i, itemStack);
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

    public Inventory inventory() {
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

}
