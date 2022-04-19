package me.magikus.core.entities.name;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityNameManager implements Listener {

    public static final Map<UUID, EntityNameLines> entityNames = new HashMap<>();

    public static EntityNameLines getLines(Entity e) {
        if (!entityNames.containsKey(e.getUniqueId())) {
            entityNames.put(e.getUniqueId(), new EntityNameLines(e));
            return entityNames.get(e.getUniqueId());
        }
        return entityNames.get(e.getUniqueId());
    }

    @EventHandler
    public void entityDie(EntityDeathEvent e) {
        getLines(e.getEntity()).killAllStands();
        entityNames.remove(e.getEntity().getUniqueId());
    }

    public static void resetArmorStands() {
        for (EntityNameLines l : entityNames.values()) {
            l.killAllStands();
        }
    }

    public static void tick() {
        for (int i = 0; i < entityNames.values().size(); i++) {
            EntityNameLines l = List.copyOf(entityNames.values()).get(i);
            if (!l.parent.isValid() || l.parent.isDead()) {
                l.killAllStands();
                entityNames.remove(l.parent.getUniqueId());
            }
        }
    }

}
