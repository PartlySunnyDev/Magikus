package me.magikus.enchants;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentManager {

    private static final Map<Enchantments, Enchant> instances = new HashMap<>();

    public static void createInstances() {
        for (Enchantments instance : Enchantments.values()) {
            try {
                instances.put(instance, instance.clazz().getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Enchant getEnchant(String s) {
        for (Enchantments e : instances.keySet()) {
            if (e.id().equals(s)) {
                return instances.get(e);
            }
        }
        return null;
    }

    public static Enchant getEnchant(Enchantments enchant) {
        return instances.get(enchant);
    }

    public static boolean doesItemHaveEnchant(ItemStack item, Enchantments e) {
        return item.getItemMeta().hasEnchant(EnchantmentManager.getEnchant(e));
    }

    public static int getEnchantLevel(ItemStack item, Enchantments e) {
        return item.getEnchantmentLevel(EnchantmentManager.getEnchant(e));
    }
}
