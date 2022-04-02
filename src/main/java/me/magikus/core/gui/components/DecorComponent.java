package me.magikus.core.gui.components;

import me.magikus.core.gui.SkyblockGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DecorComponent extends GuiComponent {
    public DecorComponent(Material showItem, SkyblockGui parent) {
        super("decor_gui", new ItemStack(showItem), parent);
        if (shownItem() != null) {
            ItemUtils.setId(shownItem(), id());
            ItemUtils.setUnstackable(shownItem());
            ItemUtils.setNameAndLore(shownItem(), " ", "");
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ItemUtils.getId(e.getCurrentItem()).equals(id())) {
            e.setCancelled(true);
        }
    }
}
