package me.magikus.core.gui.components;

import me.magikus.core.gui.SkyblockGui;
import me.magikus.core.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ButtonComponent extends GuiComponent {

    private final Consumer<InventoryClickEvent> action;
    private final UUID uniqueID = UUID.randomUUID();

    public ButtonComponent(ItemStack i, Consumer<InventoryClickEvent> action, SkyblockGui parent) {
        super("button_gui", i, parent);
        ItemUtils.setId(i, id());
        this.action = action;
    }

    private void call(InventoryClickEvent type) {
        action.accept(type);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (ItemUtils.getId(e.getCurrentItem()).equals(id()) && Objects.equals(ItemUtils.getUniqueId(e.getCurrentItem()), uniqueID)) {
            call(e);
            e.setCancelled(true);
        }
    }

    public Consumer<InventoryClickEvent> action() {
        return action;
    }
}
