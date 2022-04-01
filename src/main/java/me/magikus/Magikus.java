package me.magikus;

import me.magikus.enchants.EnchantmentManager;
import me.magikus.items.ItemManager;
import me.magikus.recipeManager.RecipeListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Magikus extends JavaPlugin {
    @Override
    public void onEnable() {
        ItemManager.createInstances();
        EnchantmentManager.createInstances();
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
