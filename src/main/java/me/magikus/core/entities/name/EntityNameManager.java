package me.magikus.core.entities.name;

import me.magikus.core.tools.util.DataUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EntityNameManager implements Listener {

    public static final Map<UUID, EntityNameLines> entityNames = new HashMap<>();

    public static EntityNameLines getLines(Entity e) {
        if (!entityNames.containsKey(e.getUniqueId())) {
            entityNames.put(e.getUniqueId(), new EntityNameLines(e));
            return entityNames.get(e.getUniqueId());
        }
        return entityNames.get(e.getUniqueId());
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

    @EventHandler
    public void entityDie(EntityDeathEvent e) {
        getLines(e.getEntity()).killAllStands();
        entityNames.remove(e.getEntity().getUniqueId());
    }

    public static void wipeOld(Chunk c) {
        Map<String, ArmorStand> exist = new HashMap<>();
        for (Entity e : c.getEntities()) {
            if (e instanceof ArmorStand && DataUtils.getData("nametagId", PersistentDataType.STRING, e) != null) {
                exist.put((String) DataUtils.getData("nametagId", PersistentDataType.STRING, e), (ArmorStand) e);
            }
        }
        for (Entity e : c.getWorld().getEntities()) {
            exist.remove(e.getUniqueId().toString());
        }
        for (ArmorStand s : exist.values()) {
            s.setSilent(true);
            s.remove();
        }
    }

}
