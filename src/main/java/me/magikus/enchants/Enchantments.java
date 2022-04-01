package me.magikus.enchants;

public enum Enchantments {

    ;

    private final String id;
    private final Class<? extends Enchant> clazz;

    Enchantments(String id, Class<? extends Enchant> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public String id() {
        return id;
    }

    public Class<? extends Enchant> clazz() {
        return clazz;
    }
}
