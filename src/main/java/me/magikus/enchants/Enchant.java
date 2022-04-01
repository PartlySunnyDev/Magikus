package me.magikus.enchants;

import me.magikus.Magikus;
import me.magikus.recipeManager.CustomRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public abstract class Enchant extends Enchantment implements Listener {
    public Enchant(@NotNull String key) {
        super(new NamespacedKey(JavaPlugin.getPlugin(Magikus.class), key));
        JavaPlugin mainPlugin = JavaPlugin.getPlugin(Magikus.class);
        if (getByKey(new NamespacedKey(JavaPlugin.getPlugin(Magikus.class), key)) == null) {
            try {
                Field fieldAcceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
                fieldAcceptingNew.setAccessible(true);
                fieldAcceptingNew.set(null, true);
                fieldAcceptingNew.setAccessible(false);
                registerEnchantment(this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        mainPlugin.getServer().getPluginManager().registerEvents(this, mainPlugin);
        mainPlugin.getServer().addRecipe(getRecipe().r());
    }

    protected abstract CustomRecipe getRecipe();


}
