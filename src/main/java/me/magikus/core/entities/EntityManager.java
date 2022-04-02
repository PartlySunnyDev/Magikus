package me.magikus.core.entities;

import java.util.HashMap;

public class EntityManager {
    private static final HashMap<String, EntityInfo> entities = new HashMap<>();

    public static void addEntity(EntityInfo entity) {
        entities.put(entity.id(), entity);
    }

    public static void removeEntity(String entityId) {
        entities.remove(entityId);
    }

    public static EntityInfo getEntity(String entityId) {
        return entities.get(entityId);
    }
}
