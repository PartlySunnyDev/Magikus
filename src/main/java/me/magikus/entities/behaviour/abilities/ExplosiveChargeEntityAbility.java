package me.magikus.entities.behaviour.abilities;

import me.magikus.Magikus;
import me.magikus.core.entities.behaviour.abilities.EntityAbility;
import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.stats.EntityStatType;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static me.magikus.core.entities.damage.DamageManager.getHitDamageOn;

public class ExplosiveChargeEntityAbility extends EntityAbility {
    public ExplosiveChargeEntityAbility() {
        super("explosive_charge");
    }

    @Override
    protected int getCastFrequency() {
        return 80;
    }

    @Override
    protected boolean doesShowParticlesBeforeCast() {
        return true;
    }

    @Override
    protected int shownParticlesTime() {
        return 20;
    }

    @Override
    protected Consumer<Entity> getAction() {
        return entity -> {
            List<Entity> entities = entity.getNearbyEntities(20, 4, 20);

            if (entities.size() < 1) {
                return;
            }

            double soFar = Double.MAX_VALUE;
            Entity closestEntity = null;

            for (Entity e : entities) {
                if (!(e instanceof Player)) {
                    continue;
                }
                double distance = e.getLocation().distance(entity.getLocation());
                if (distance < soFar) {
                    soFar = distance;
                    closestEntity = e;
                }
            }

            Vector target = closestEntity.getLocation().toVector();
            Vector movement = target.subtract(entity.getLocation().toVector());
            movement.multiply(0.5f);
            entity.setVelocity(movement);
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 4, 1);
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                entity.getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getLocation(), 10, 0.7, 0.3, 0.7);
                entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 10, 0.7, 0.3, 0.7);
            }, 0, 2);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                task.cancel();
                List<Entity> newEntities = entity.getNearbyEntities(2, 2, 2);
                for (Entity e : newEntities) {
                    if (e instanceof Player p) {
                        DamageManager.damagePlayer(p, getHitDamageOn(p, EntityStatType.getStat(entity, EntityStatType.DAMAGE) * 10, false), EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, "Charge Explosion");
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                        p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 3, 0.5, 0.5, 0.5);
                    }
                }
            }, 10);
        };
    }

    @Override
    protected Predicate<Entity> getPredicate() {
        return entity -> {
            List<Entity> entities = entity.getNearbyEntities(20, 4, 20);
            for (Entity e : entities) {
                if (!(e instanceof Player)) {
                    continue;
                }
                return true;
            }
            return false;
        };
    }

    @Override
    protected int delayTime() {
        return 20;
    }
}
