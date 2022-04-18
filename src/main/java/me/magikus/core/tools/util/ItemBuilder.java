package me.magikus.core.tools.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack i;
    private final ItemMeta meta;

    public ItemBuilder(Material m) {
        i = new ItemStack(m);
        meta = i.getItemMeta();
    }

    public static ItemBuilder builder(Material m) {
        return new ItemBuilder(m);
    }

    public ItemBuilder setLore(String... lore) {
        meta.setLore(List.of(lore));
        return this;
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemStack build() {
        i.setItemMeta(meta);
        return i;
    }

}
