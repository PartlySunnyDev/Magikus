package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.tools.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TextedDecorComponent extends GuiComponent {
    public TextedDecorComponent(ItemStack i, MagikusGui parent, String name, String lore) {
        super("texted_gui", i, parent);
        ItemStack a = shownItem();
        if (a == null) {
            return;
        }
        ItemUtils.setId(a, id());
        ItemUtils.setNameAndLore(a, name, lore);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (ItemUtils.getId(e.getCurrentItem()).equals(id())) {
            e.setCancelled(true);
        }
    }
}
