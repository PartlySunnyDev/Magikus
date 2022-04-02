package me.magikus.core.enums;

public enum VanillaEntityDamageAttributes {


    AXOLOTL(2),
    BEE(2),
    BLAZE(4),
    CAVE_SPIDER(2),
    DOLPHIN(2),
    DROWNED(2),
    ELDER_GUARDIAN(2),
    ENDER_DRAGON(6),
    ENDERMAN(4),
    ENDERMITE(2),
    FOX(2),
    EVOKER_FANGS(6),
    GOAT(1),
    GUARDIAN(2),
    HOGLIN(3),
    HUSK(2),
    IRON_GOLEM(10),
    LLAMA_SPIT(1),
    MAGMA_CUBE(3),
    PANDA(4),
    PHANTOM(2),
    PIGLIN(4),
    PIGLIN_BRUTE(8),
    POLAR_BEAR(4),
    PUFFERFISH(2),
    RAVAGER(7),
    SHULKER(4),
    SILVERFISH(1),
    SLIME(2),
    SPIDER(2),
    VEX(6),
    VINDICATOR(8),
    WITHER_SKELETON(5),
    WOLF(3),
    ZOGLIN(4),
    ZOMBIE(3),
    ZOMBIFIED_PIGLIN(5),
    ZOMBIE_VILLAGER(3),
    SKELETON(4),
    STRAY(4),
    PILLAGER(4),
    ;


    private final int value;

    VanillaEntityDamageAttributes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
