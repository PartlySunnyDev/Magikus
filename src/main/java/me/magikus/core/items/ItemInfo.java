package me.magikus.core.items;

public record ItemInfo(String id,
                       Class<? extends MagikusItem> itemType,
                       ItemType type) {
}
