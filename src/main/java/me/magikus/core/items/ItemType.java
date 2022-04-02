package me.magikus.core.items;

public enum ItemType {
    ITEM("", false, false),
    SWORD("sword", true, false),
    BOW("bow", true, false),
    HELMET("helmet", true, false),
    CHESTPLATE("chestplate", true, false),
    LEGGINGS("leggings", true, false),
    BOOTS("boots", true, false),
    AXE("axe", true, false),
    PICKAXE("pickaxe", true, false),
    HOE("hoe", true, false),
    SHOVEL("shovel", true, false),
    ACCESSORY("accessory", true, false),
    HATCCESORY("hatccesory", true, false);

    private final String display;
    private final boolean reforgable;
    private final boolean isDungeon;

    ItemType(String display, boolean reforgable, boolean isDungeon) {
        this.display = display;
        this.reforgable = reforgable;
        this.isDungeon = isDungeon;
    }

    public String display() {
        return display;
    }

    public boolean reforgable() {
        return reforgable;
    }

    public boolean isDungeon() {
        return isDungeon;
    }
}
