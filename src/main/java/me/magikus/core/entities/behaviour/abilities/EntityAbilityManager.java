package me.magikus.core.entities.behaviour.abilities;

import java.util.HashMap;
import java.util.Map;

public class EntityAbilityManager {

    private static final Map<String, EntityAbility> entityAbilities = new HashMap<>();

    public static void registerEntityAbility(EntityAbility e) {
        entityAbilities.put(e.id(), e);
    }

    public static void unregisterEntityAbility(String id) {
        entityAbilities.remove(id);
    }

    public static EntityAbility getRegisteredAbility(String id) {
        return entityAbilities.get(id);
    }

}
