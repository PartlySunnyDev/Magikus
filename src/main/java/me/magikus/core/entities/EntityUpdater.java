package me.magikus.core.entities;

import me.magikus.Magikus;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.tools.reflection.JavaAccessor;
import me.magikus.core.tools.util.EntityUtils;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class EntityUpdater implements Listener {

    public EntityUpdater(Server s) {
        new ConstantUpdater(s).runTaskTimer(JavaPlugin.getPlugin(Magikus.class), 0, 20);
        new PlayerAttackStrengthUpdater(s).runTaskTimer(JavaPlugin.getPlugin(Magikus.class), 0, 4);
    }

    public static void updateStats(Entity e) {
        EntityUtils.repairEntity(e);
        if (e instanceof LivingEntity) {
            ((LivingEntity) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(EntityStatType.getStatWithBonus(e, EntityStatType.SPEED).floatValue() / 500f);
        }
    }

    public static void updateName(Entity e) {
        e.setCustomName(EntityUtils.getDisplayName(e));
        e.setCustomNameVisible(true);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().isAlive() || entity.getType() == EntityType.ARMOR_STAND || entity.getType() == EntityType.PLAYER) {
            return;
        }
        updateStats(entity);
        updateName(entity);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity e : event.getChunk().getEntities()) {
            if (!e.getType().isAlive() || e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.PLAYER) {
                return;
            }
            updateStats(e);
            updateName(e);
        }
    }

}

class PlayerAttackStrengthUpdater extends BukkitRunnable {

    private final Server s;

    public PlayerAttackStrengthUpdater(Server s) {
        this.s = s;
    }

    @Override
    public void run() {
        for (Player p : s.getOnlinePlayers()) {
            ServerPlayer cp = ((CraftPlayer) p).getHandle();
            Field aQ = JavaAccessor.getField(net.minecraft.world.entity.LivingEntity.class, "aQ");
            if ((int) JavaAccessor.getValue(cp, aQ) > cp.getCurrentItemAttackStrengthDelay() * 0.9 - 0.5) {
                JavaAccessor.setValue(cp, aQ, cp.getCurrentItemAttackStrengthDelay() * 0.9 - 0.5);
            }
        }
    }
}

class ConstantUpdater extends BukkitRunnable {

    private final Server s;

    public ConstantUpdater(Server s) {
        this.s = s;
    }

    @Override
    public void run() {
        for (World w : s.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (!(!e.getType().isAlive() || e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.PLAYER)) {
                    EntityUpdater.updateStats(e);
                    EntityUpdater.updateName(e);
                }
            }
        }
    }
}
