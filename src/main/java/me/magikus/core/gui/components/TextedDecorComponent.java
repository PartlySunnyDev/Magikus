package me.magikus.core.gui.components;

import me.magikus.core.gui.SkyblockGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TextedDecorComponent extends GuiComponent {
    public TextedDecorComponent(ItemStack i, SkyblockGui parent) {
        super("texted_gui", i, parent);
        ItemUtils.setId(shownItem(), id());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ItemUtils.getId(e.getCurrentItem()).equals(id())) {
            e.setCancelled(true);
        }
    }
}
