package me.magikus.core.items.additions.enchants;

import java.util.HashMap;

public class EnchantManager {

    private static final HashMap<String, Enchant> enchants = new HashMap<>();

    public static void addEnchant(Enchant enchant) {
        enchants.put(enchant.id(), enchant);
    }

    public static void removeEnchant(String enchantID) {
        enchants.remove(enchantID);
    }

    public static Enchant getEnchant(String enchantID) {
        return enchants.get(enchantID);
    }

}
