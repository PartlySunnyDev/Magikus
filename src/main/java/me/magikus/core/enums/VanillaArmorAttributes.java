package me.magikus.core.enums;

public enum VanillaArmorAttributes {

    LEATHER_HELMET(5),
    LEATHER_CHESTPLATE(15),
    LEATHER_LEGGINGS(10),
    LEATHER_BOOTS(5),
    GOLD_HELMET(10),
    GOLD_CHESTPLATE(25),
    GOLD_LEGGINGS(15),
    GOLD_BOOTS(5),
    DIAMOND_HELMET(15),
    DIAMOND_CHESTPLATE(40),
    DIAMOND_LEGGINGS(30),
    DIAMOND_BOOTS(15),
    IRON_HELMET(10),
    IRON_CHESTPLATE(30),
    IRON_LEGGINGS(25),
    IRON_BOOTS(10),
    CHAINMAIL_HELMET(12),
    CHAINMAIL_CHESTPLATE(30),
    CHAINMAIL_LEGGINGS(20),
    CHAINMAIL_BOOTS(7),
    NETHERITE_HELMET(20),
    NETHERITE_CHESTPLATE(50),
    NETHERITE_LEGGINGS(35),
    NETHERITE_BOOTS(20),
    ;

    private int defense;

    VanillaArmorAttributes(int defense) {
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
