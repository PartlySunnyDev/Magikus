package me.magikus.core.gui.components;

import me.magikus.core.gui.SkyblockGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class ToggleableComponent extends GuiComponent {

    private final ItemStack onItem;
    private final ItemStack offItem;
    private final UUID uniqueID = UUID.randomUUID();
    private boolean toggleOn = false;

    public ToggleableComponent(ItemStack off, ItemStack on, SkyblockGui parent) {
        super("toggleable_gui", off, parent);
        ItemUtils.setUniqueId(off, uniqueID);
        ItemUtils.setUniqueId(on, uniqueID);
        this.onItem = on;
        this.offItem = off;
    }

    public void toggle() {
        toggleOn = !toggleOn;
    }

    @EventHandler
    public void onToggle(InventoryClickEvent e) {
        if (ItemUtils.getId(e.getCurrentItem()).equals(id()) && Objects.equals(ItemUtils.getUniqueId(e.getCurrentItem()), uniqueID)) {
            toggle();
            if (e.getClickedInventory() != null) {
                if (toggleOn) {
                    e.getClickedInventory().setItem(e.getSlot(), onItem);
                } else {
                    e.getClickedInventory().setItem(e.getSlot(), offItem);
                }
            }
            e.setCancelled(true);
        }
    }
}
