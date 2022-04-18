package me.magikus.core.gui.components;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.ConsoleLogger;
import me.magikus.core.gui.GuiManager;
import me.magikus.core.gui.MagikusGui;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StorageComponent extends GuiComponent {

    private boolean empty = true;
    private ItemStack emptyItem = null;
    private int slot = -1;

    public StorageComponent(MagikusGui parent) {
        super("storage_gui", null, parent);
    }

    public StorageComponent(MagikusGui parent, ItemStack shownItem) {
        super("storage_gui", shownItem, parent);
        emptyItem = shownItem;
        NBTItem nbti = new NBTItem(emptyItem);
        nbti.setBoolean("isgui", true);
        nbti.applyNBT(emptyItem);
        checkEmpty();
    }

    @EventHandler
    public void onSlotClick(InventoryClickEvent e) {
        checkEmpty();
        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (e.getSlot() == slot && clickedInventory.equals(GuiManager.getActiveInventory(e.getWhoClicked().getUniqueId()).inventory())) {
            ConsoleLogger.console("Storage slot clicked!");
            ItemStack current = e.getCurrentItem();
            final HumanEntity whoClicked = e.getWhoClicked();
            switch (e.getAction()) {
                case SWAP_WITH_CURSOR -> {
                    ItemStack newInventory = whoClicked.getItemOnCursor().clone();
                    ItemStack newCursor = e.getCurrentItem().clone();
                    whoClicked.setItemOnCursor(newCursor);
                    setShownItem(newInventory);
                }
                case COLLECT_TO_CURSOR, MOVE_TO_OTHER_INVENTORY -> {
                    setShownItem(null);
                    checkEmpty();
                    parent.updateInventory();
                    return;
                }
                case DROP_ALL_SLOT -> {
                    setShownItem(null);
                    whoClicked.getWorld().dropItem(whoClicked.getLocation(), e.getCurrentItem());
                }
                case DROP_ONE_SLOT -> {
                    ItemStack newInventory = e.getCurrentItem().clone();
                    newInventory.setAmount(newInventory.getAmount() - 1);
                    setShownItem(newInventory);
                    ItemStack dropped = e.getCurrentItem().clone();
                    dropped.setAmount(1);
                    whoClicked.getWorld().dropItem(whoClicked.getLocation(), dropped);
                }
                case HOTBAR_SWAP -> {
                    if (!empty) {
                        return;
                    }
                }
                case PICKUP_ALL -> {
                    if (!empty) {
                        clickedInventory.setItem(e.getSlot(), null);
                        whoClicked.setItemOnCursor(current);
                        setShownItem(emptyItem);
                    }
                }
                case PICKUP_HALF -> {
                    if (current != null && !empty) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() / 2);
                        newCursor.setAmount(current.getAmount() / 2);
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        whoClicked.setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PICKUP_SOME -> {
                    if (current != null && !empty) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() - current.getMaxStackSize());
                        newCursor.setAmount(current.getMaxStackSize());
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        whoClicked.setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PICKUP_ONE -> {
                    if (current != null && !empty) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newInventory.setAmount(current.getAmount() - 1);
                        newCursor.setAmount(whoClicked.getItemOnCursor().getAmount() + 1);
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        whoClicked.setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PLACE_ALL -> {
                    clickedInventory.setItem(e.getSlot(), current);
                    setShownItem(current);
                    whoClicked.setItemOnCursor(null);
                }
                case PLACE_SOME -> {
                    if (whoClicked.getItemOnCursor().hasItemMeta()) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newCursor.setAmount(whoClicked.getItemOnCursor().getAmount() - (current.getAmount() - current.getMaxStackSize()));
                        newInventory.setAmount(current.getMaxStackSize());
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        whoClicked.setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
                case PLACE_ONE -> {
                    if (whoClicked.getItemOnCursor().hasItemMeta()) {
                        ItemStack newInventory = current.clone();
                        ItemStack newCursor = current.clone();
                        newCursor.setAmount(whoClicked.getItemOnCursor().getAmount() - 1);
                        newInventory.setAmount(current.getAmount() + 1);
                        clickedInventory.setItem(e.getSlot(), newInventory);
                        whoClicked.setItemOnCursor(newCursor);
                        setShownItem(newInventory);
                    }
                }
            }
            e.setCancelled(true);
            checkEmpty();
            parent.updateInventory();
        }
    }

    public ItemStack emptyItem() {
        return emptyItem;
    }

    public boolean empty() {
        return empty;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    private void checkEmpty() {
        ItemStack shownItem = shownItem();
        boolean b = shownItem == null;
        if (b || !shownItem.hasItemMeta()) {
            empty = true;
            setShownItem(emptyItem);
            return;
        }
        empty = new NBTItem(shownItem).hasKey("isgui");
    }
}
