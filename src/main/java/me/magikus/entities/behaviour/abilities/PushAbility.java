package me.magikus.entities.behaviour.abilities;

import me.magikus.core.entities.behaviour.abilities.EntityAbility;
import me.magikus.core.tools.util.BukkitUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PushAbility extends EntityAbility {
    public PushAbility() {
        super("push");
    }

    @Override
    protected int getCastFrequency() {
        return 100;
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
            Collection<Entity> damagedEntities = entity.getWorld().getNearbyEntities(entity.getLocation(), 16, 0.5, 16);
            for (Entity e : damagedEntities) {
                if (e != entity) {
                    Vector flingDirection = entity.getLocation().toVector().subtract(e.getLocation().toVector());
                    if (flingDirection.getX() == 0) {
                        flingDirection.setX(0.01);
                    }
                    if (flingDirection.getY() == 0) {
                        flingDirection.setY(0.01);
                    }
                    if (flingDirection.getZ() == 0) {
                        flingDirection.setZ(0.01);
                    }
                    flingDirection.normalize().multiply(-2);
                    flingDirection.setY(flingDirection.getY() + 1);
                    e.setVelocity(flingDirection);
                }
            }
            BukkitUtils.scheduleRepeatingCancelTask(() -> {
                for (Entity e : damagedEntities) {
                    if (e instanceof Player p) {
                        p.playSound(p.getLocation(), Sound.BLOCK_METAL_PLACE, 2, 1);
                    }
                    e.getWorld().spawnParticle(Particle.ASH, e.getLocation(), 6, 0.5, 0.5, 0.5);
                    e.getWorld().spawnParticle(Particle.WHITE_ASH, e.getLocation(), 6, 0.5, 0.5, 0.5);
                }
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 12, 1, 1, 1);
            }, 0, 1, 15);
        };
    }

    @Override
    protected Predicate<Entity> getPredicate() {
        return entity -> {
            List<Entity> entities = entity.getNearbyEntities(16, 4, 16);
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
        return 10;
    }
}
