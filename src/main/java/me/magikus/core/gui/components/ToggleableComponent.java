package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.tools.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ToggleableComponent extends GuiComponent {

    private final ItemStack onItem;
    private final ItemStack offItem;
    private boolean toggleOn = false;
    private Sound soundOnToggle = null;

    public ToggleableComponent(ItemStack off, ItemStack on, MagikusGui parent) {
        super("toggleable_gui", off, parent);
        ItemUtils.setId(off, id());
        ItemUtils.setId(on, id());
        ItemUtils.setUniqueId(off, uniqueID);
        ItemUtils.setUniqueId(on, uniqueID);
        this.onItem = on;
        this.offItem = off;
    }

    public ToggleableComponent(ItemStack off, ItemStack on, MagikusGui parent, Sound soundOnToggle) {
        this(off, on, parent);
        this.soundOnToggle = soundOnToggle;
    }

    public void toggle() {
        toggleOn = !toggleOn;
    }

    public boolean state() {
        return toggleOn;
    }

    @EventHandler
    public void onToggle(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (ItemUtils.getId(e.getCurrentItem()).equals(id()) && Objects.equals(ItemUtils.getUniqueId(e.getCurrentItem()), uniqueID)) {
            if (soundOnToggle != null) {
                if (e.getWhoClicked() instanceof Player p) {
                    p.playSound(p, soundOnToggle, 1, 1);
                }
            }
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
