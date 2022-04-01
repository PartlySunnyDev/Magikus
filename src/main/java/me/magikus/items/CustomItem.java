package me.magikus.items;

public enum CustomItem {

    ;
    private final String id;
    private final Class<? extends CustomItemBuilder> builder;

    CustomItem(String id, Class<? extends CustomItemBuilder> builder) {
        this.id = id;
        this.builder = builder;
    }

    public String id() {
        return id;
    }

    public Class<? extends CustomItemBuilder> builder() {
        return builder;
    }
}
