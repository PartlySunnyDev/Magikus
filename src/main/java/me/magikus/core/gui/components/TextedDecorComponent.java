package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TextedDecorComponent extends GuiComponent {
    public TextedDecorComponent(ItemStack i, MagikusGui parent) {
        super("texted_gui", i, parent);
        ItemUtils.setId(shownItem(), id());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (ItemUtils.getId(e.getCurrentItem()).equals(id())) {
            e.setCancelled(true);
        }
    }
}
