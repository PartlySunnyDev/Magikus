package me.magikus.core.items.additions;

import java.util.HashMap;

public class AdditionManager {

    private static final HashMap<String, AdditionInfo> additions = new HashMap<>();

    public static void addAddition(AdditionInfo addition) {
        additions.put(addition.id(), addition);
    }

    public static void removeAddition(String additionId) {
        additions.remove(additionId);
    }

    public static AdditionInfo getAddition(String additionId) {
        return additions.get(additionId);
    }

}
