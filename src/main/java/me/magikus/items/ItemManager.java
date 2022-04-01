package me.magikus.items;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private static final Map<CustomItem, ItemStack> instances = new HashMap<>();

    public static void createInstances() {
        for (CustomItem instance : CustomItem.values()) {
            try {
                ItemStack item = (ItemStack) instance.builder().getMethod("build").invoke(instance.builder().getDeclaredConstructor().newInstance());
                instances.put(instance, item);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ItemStack getItem(String s) {
        for (CustomItem e : instances.keySet()) {
            if (e.id().equals(s)) {
                return instances.get(e);
            }
        }
        return null;
    }

    public static ItemStack getItem(CustomItem item) {
        return instances.get(item);
    }
}
