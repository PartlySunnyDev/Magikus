package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class StorageComponent extends GuiComponent {

    private final UUID uniqueId = UUID.randomUUID();

    public StorageComponent(MagikusGui parent) {
        super("storage", null, parent);
    }

    @EventHandler
    public void onSlotClick(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }
        if (ItemUtils.getId(e.getCurrentItem()).equals(id()) && Objects.equals(ItemUtils.getUniqueId(e.getCurrentItem()), uniqueId)) {
            ItemStack current = e.getCurrentItem();
            switch (e.getAction()) {
                case PICKUP_ALL -> {
                    clickedInventory.setItem(e.getSlot(), null);
                    setShownItem(null);
                    e.getWhoClicked().setItemOnCursor(current);
                }
                case PICKUP_HALF -> {
                    if (current != null) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() / 2);
                        newCursor.setAmount(current.getAmount() / 2);
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        e.getWhoClicked().setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PICKUP_SOME -> {
                    if (current != null) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() - current.getMaxStackSize());
                        newCursor.setAmount(current.getMaxStackSize());
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        e.getWhoClicked().setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PICKUP_ONE -> {
                    if (current != null) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() - 1);
                        newCursor.setAmount(current.getMaxStackSize());
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        e.getWhoClicked().setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
            }
            e.setCancelled(true);
            parent.updateInventory();
        }
    }
}
