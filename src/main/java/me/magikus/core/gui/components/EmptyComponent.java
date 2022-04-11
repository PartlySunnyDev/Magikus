package me.magikus.core.gui.components;

import me.magikus.core.gui.MagikusGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmptyComponent extends GuiComponent {
    public EmptyComponent(MagikusGui parent) {
        super("empty_gui", new ItemStack(Material.AIR), parent);
    }
}
