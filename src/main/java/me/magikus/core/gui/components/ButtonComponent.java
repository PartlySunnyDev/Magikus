package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ButtonComponent extends GuiComponent {

    private final Consumer<InventoryClickEvent> action;
    private final UUID uniqueID = UUID.randomUUID();

    public ButtonComponent(ItemStack i, Consumer<InventoryClickEvent> action, MagikusGui parent) {
        super("button_gui", i, parent);
        ItemUtils.setId(i, id());
        ItemUtils.setUniqueId(i, uniqueID);
        this.action = action;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (ItemUtils.getId(e.getCurrentItem()).equals(id()) && Objects.equals(ItemUtils.getUniqueId(e.getCurrentItem()), uniqueID)) {
            action.accept(e);
            e.setCancelled(true);
        }
    }

}
