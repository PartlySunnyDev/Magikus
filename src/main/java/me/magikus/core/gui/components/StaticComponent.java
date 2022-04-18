package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.tools.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class StaticComponent extends GuiComponent {
    public StaticComponent(Material showItem, MagikusGui parent) {
        super("static_gui", new ItemStack(showItem), parent);
        if (shownItem() != null) {
            ItemUtils.setId(shownItem(), id());
            ItemUtils.setUnstackable(shownItem());
            ItemUtils.setNameAndLore(shownItem(), " ", "");
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (ItemUtils.getId(e.getCurrentItem()).equals(id())) {
            e.setCancelled(true);
        }
    }
}
