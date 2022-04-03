package me.magikus.core.items.additions.enchants;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.MagikusItem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnchantList {

    private final Map<String, Enchant> enchantList = new HashMap<>();
    private MagikusItem parent;

    public EnchantList() {
    }

    public EnchantList(MagikusItem parent) {
        this.parent = parent;
    }

    /**
     * Adds an enchantment or combines it with the existing one
     *
     * @param enchantId The id of the added enchantment
     * @param level     The level of the enchantment
     */
    public void addEnchant(String enchantId, int level) {
        Enchant enchant = EnchantManager.getEnchant(enchantId);
        if (enchantId == null) {
            return;
        }
        if (parent != null && enchant.cannotApply(parent)) {
            return;
        }
        Enchant target = enchantList.get(enchantId);
        for (String a : enchantList.keySet()) {
            if (a.equals(enchantId)) {
                target = enchantList.get(a);
            }
        }
        if (target == null) {
            try {
                Enchant value;
                value = enchant.cl().getDeclaredConstructor(MagikusItem.class, Integer.class).newInstance(parent, 1);
                value.setLevel(level);
                enchantList.put(enchantId, value);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                IllegalAccessException e) {
                e.printStackTrace();
            }
            if (parent != null) {
                parent.updateMagikusItem();
            }
            return;
        }
        target.setLevel(target.level() > level ? target.level() : (target.level() == level ? level + 1 : level));
        if (target.level() > target.maxLevel()) {
            target.setLevel(target.maxLevel());
        }
        if (checkZero(target)) {
            enchantList.remove(enchantId);
        }
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public int getLevelOf(String enchantId) {
        Enchant enchant = enchantList.get(enchantId);
        if (enchant != null) {
            return enchant.level();
        } else {
            return 0;
        }
    }

    public void addEnchant(NBTCompound enchants) {
        if (enchants == null) {
            return;
        }
        for (String s : enchants.getKeys()) {
            addEnchant(s, enchants.getInteger(s));
        }
    }

    public void setList(EnchantList newList) {
        enchantList.clear();
        enchantList.putAll(newList.enchantList);
    }

    public void addEnchant(String type) {
        addEnchant(type, 1);
    }

    public void addEnchant(Enchant enchant) {
        if (parent != null && enchant.cannotApply(parent)) {
            return;
        }
        Enchant target = enchantList.get(enchant.id());
        if (target == null) {
            enchantList.put(enchant.id(), enchant);
            if (parent != null) {
                parent.updateMagikusItem();
            }
            return;
        }
        target.setLevel(target.level() > enchant.level() ? target.level() : (target.level() == enchant.level() ? enchant.level() + 1 : enchant.level()));
        if (target.level() > target.maxLevel()) {
            target.setLevel(target.maxLevel());
        }
        if (checkZero(target)) {
            enchantList.remove(enchant.id());
        }
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public void completelyRemove(String enchantId) {
        enchantList.remove(enchantId);
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public NBTItem applyEnchants(NBTItem item) {
        NBTCompound c = item.getOrCreateCompound("enchants");
        for (Enchant a : enchantList.values()) {
            if (!item.getBoolean("mg_unique")) {
                item.setBoolean("mg_unique", true);
            }
            c.setInteger(a.id(), (c.getInteger(a.id()) == null ? a.level() : c.getInteger(a.id())) + a.level());
        }
        return item;
    }

    public Enchant[] asList() {
        Enchant[] returned = new Enchant[enchantList.size()];
        int count = 0;
        for (Enchant s : enchantList.values()) {
            returned[count] = s;
            count++;
        }
        return returned;
    }

    public Map<String, Enchant> additionList() {
        return enchantList;
    }

    public ArrayList<Enchant> asArrayList() {
        return new ArrayList<>(enchantList.values());
    }

    public boolean checkZero(Enchant a) {
        return a.level() < 1;
    }
}
