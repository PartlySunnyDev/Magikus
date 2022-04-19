package me.magikus.guis;

import me.magikus.core.ConsoleLogger;
import me.magikus.core.gui.MagikusGui;
import me.magikus.core.gui.components.*;
import me.magikus.core.tools.classes.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class TestGui extends MagikusGui {

    public TestGui() {
        super("test", 9, "Test GUI");
    }

    @Override
    protected void buildGui() {
        setComponent(0, new StaticComponent(Material.DIAMOND, this));
        setComponent(1, new ButtonComponent(new ItemStack(Material.OAK_BUTTON), (event -> {
            ConsoleLogger.console("Button component pressed");
        }), this, Sound.UI_BUTTON_CLICK));
        setComponent(2, new StorageComponent(this, ItemBuilder.builder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Insert a coin!").setLore(ChatColor.GRAY + "Coins are not even real XD").build()));
        setComponent(3, new TextedDecorComponent(new ItemStack(Material.DIRT), this, ChatColor.RED + "Dirty Dirt", "Very dirty!"));
        setComponent(4, new ToggleableComponent(new ItemStack(Material.RED_WOOL), new ItemStack(Material.GREEN_WOOL), this, Sound.BLOCK_ANVIL_PLACE));
    }

}
