package me.magikus.entities.behaviour.abilities;

import me.magikus.core.entities.behaviour.abilities.EntityAbility;
import me.magikus.core.tools.util.BukkitUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PullAbility extends EntityAbility {
    public PullAbility() {
        super("pull");
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
            List<Entity> damagedEntities = List.copyOf(entity.getWorld().getNearbyEntities(entity.getLocation(), 16, 0.5, 16));
            BukkitUtils.scheduleRepeatingCancelTask(() -> {
                for (Entity e : damagedEntities) {
                    e.setVelocity(entity.getLocation().toVector().subtract(e.getLocation().toVector()).multiply(0.8));
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
