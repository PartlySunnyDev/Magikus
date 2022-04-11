package me.magikus.guis;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.gui.components.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TestGui extends MagikusGui {

    protected TestGui() {
        super("test", 9, "Test GUI");
    }

    @Override
    protected void buildGui() {
        contents.set(0, new DecorComponent(Material.DIAMOND, this));
        contents.set(1, new ButtonComponent(new ItemStack(Material.OAK_BUTTON), (event -> {
            System.out.println("Button component pressed");
        }), this));
        contents.set(2, new StorageComponent(this));
        contents.set(3, new TextedDecorComponent(new ItemStack(Material.DIRT), this));
        contents.set(4, new ToggleableComponent(new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), this));
    }

}
