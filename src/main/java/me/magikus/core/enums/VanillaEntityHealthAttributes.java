package me.magikus.core.enums;

public enum VanillaEntityHealthAttributes {

    AXOLOTL(14),
    BAT(6),
    CAT(10),
    CHICKEN(4),
    COD(3),
    COW(10),
    DONKEY(20),
    FOX(10),
    GLOW_SQUID(10),
    HORSE(20),
    MOOSHROOM(10),
    OCELOT(10),
    PARROT(6),
    PIG(10),
    PUFFERFISH(3),
    RABBIT(3),
    SHEEP(8),
    SKELETON_HORSE(15),
    SNOW_GOLEM(4),
    SQUID(10),
    STRIDER(20),
    TURTLE(30),
    VILLAGER(20),
    WANDERING_TRADER(20),
    BEE(10),
    CAVE_SPIDER(12),
    DOLPHIN(10),
    ENDERMAN(40),
    GOAT(10),
    LLAMA(20),
    PANDA(20),
    PIGLIN(16),
    POLAR_BEAR(30),
    SPIDER(16),
    WOLF(20),
    ZOMBIFIED_PIGLIN(20),
    MULE(20),
    SALMON(3),
    TROPICAL_FISH(3),
    IRON_GOLEM(100),
    TRADER_LLAMA(20),
    BLAZE(20),
    CREEPER(20),
    DROWNED(20),
    ELDER_GUARDIAN(80),
    ENDERMITE(8),
    GHAST(10),
    GUARDIAN(30),
    HOGLIN(40),
    HUSK(20),
    MAGMA_CUBE(8),
    PIGLIN_BRUTE(50),
    PILLAGER(24),
    RAVAGER(100),
    SHULKER(30),
    SILVERFISH(8),
    SLIME(8),
    STRAY(20),
    VEX(14),
    VINDICATOR(24),
    WITCH(26),
    ZOGLIN(40),
    ZOMBIE(20),
    ZOMBIE_VILLAGER(20),
    EVOKER(24),
    PHANTOM(20),
    SKELETON(20),
    WITHER_SKELETON(20),
    ENDER_DRAGON(200),
    WITHER(300),
    GIANT(100),
    ILLUSIONER(32),
    ZOMBIE_HORSE(15);

    private final int value;

    VanillaEntityHealthAttributes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
