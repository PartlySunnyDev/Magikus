package me.magikus.core.items.additions.ascensions;

import java.util.HashMap;

public class AscensionManager {

    private static final HashMap<String, Ascension> ascensions = new HashMap<>();

    public static void addAscension(Ascension ascension) {
        ascensions.put(ascension.id(), ascension);
    }

    public static void removeAscension(String ascensionId) {
        ascensions.remove(ascensionId);
    }

    public static Ascension getAscension(String ascensionId) {
        return ascensions.get(ascensionId);
    }

}
