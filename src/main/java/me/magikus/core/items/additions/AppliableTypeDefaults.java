package me.magikus.core.items.additions;

import me.magikus.core.items.ItemType;

public class AppliableTypeDefaults {
    public static final ItemType[] rangedWeapons = new ItemType[]{ItemType.BOW};
    public static final ItemType[] meleeWeapons = new ItemType[]{ItemType.SWORD};
    public static final ItemType[] armor = new ItemType[]{ItemType.HELMET, ItemType.CHESTPLATE, ItemType.BOOTS, ItemType.LEGGINGS};
    public static final ItemType[] accessories = new ItemType[]{ItemType.HATCCESORY, ItemType.ACCESSORY};
    public static final ItemType[] tools = new ItemType[]{ItemType.AXE, ItemType.PICKAXE, ItemType.SHOVEL, ItemType.HOE};
    public static final ItemType[] all = ItemType.values();
}
