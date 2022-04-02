package me.magikus.core.items.additions.reforges;

import java.util.HashMap;

public class ReforgeManager {

    private static final HashMap<String, Reforge> reforges = new HashMap<>();

    public static void addReforge(Reforge reforge) {
        reforges.put(reforge.id(), reforge);
    }

    public static void removeReforge(String reforgeId) {
        reforges.remove(reforgeId);
    }

    public static Reforge getReforge(String reforgeId) {
        return reforges.get(reforgeId);
    }

}
