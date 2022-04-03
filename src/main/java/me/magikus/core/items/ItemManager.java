package me.magikus.core.items;

import me.magikus.core.ConsoleLogger;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    public static final Map<String, ItemInfo> itemList = new HashMap<>();

    public static void addItem(ItemInfo stat) {
        itemList.put(stat.id(), stat);
    }

    public static void removeItem(ItemInfo type) {
        itemList.remove(type.id());
    }

    public static ItemInfo getInfoFromId(String id) {
        return itemList.get(id);
    }

    public static MagikusItem getInstance(ItemInfo type) {
        try {
            return type.itemType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
            NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MagikusItem getInstance(ItemInfo type, Player p) {
        try {
            return type.itemType().getDeclaredConstructor(Player.class).newInstance(p);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
            NoSuchMethodException e) {
            ConsoleLogger.console("This item has no player constructor! ITEM_ID: " + type.id());
        }
        return null;
    }

}
