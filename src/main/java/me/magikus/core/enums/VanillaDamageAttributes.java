package me.magikus.core.enums;

public enum VanillaDamageAttributes {

    WOODEN_SWORD(4),
    GOLDEN_SWORD(4),
    DIAMOND_SWORD(7),
    IRON_SWORD(6),
    STONE_SWORD(5),
    NETHERITE_SWORD(8),
    TRIDENT(9),
    WOODEN_SHOVEL(2),
    GOLDEN_SHOVEL(2),
    DIAMOND_SHOVEL(5),
    IRON_SHOVEL(4),
    STONE_SHOVEL(3),
    NETHERITE_SHOVEL(6),
    WOODEN_PICKAXE(2),
    GOLDEN_PICKAXE(2),
    DIAMOND_PICKAXE(5),
    IRON_PICKAXE(4),
    STONE_PICKAXE(3),
    NETHERITE_PICKAXE(6),
    WOODEN_AXE(7),
    GOLDEN_AXE(7),
    DIAMOND_AXE(9),
    IRON_AXE(9),
    STONE_AXE(9),
    NETHERITE_AXE(10),
    WOODEN_HOE(1),
    GOLDEN_HOE(1),
    DIAMOND_HOE(4),
    IRON_HOE(3),
    STONE_HOE(2),
    NETHERITE_HOE(4);

    private int damage;

    VanillaDamageAttributes(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
