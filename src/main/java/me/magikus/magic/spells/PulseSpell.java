package me.magikus.magic.spells;

import me.magikus.core.magic.spells.Spell;
import me.magikus.core.tools.classes.Requirements;
import me.magikus.core.tools.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PulseSpell extends Spell {
    public PulseSpell() {
        super("pulse", "Send a pulse which pushes enemies away", ChatColor.AQUA + "Pulse", new Requirements(), 30);
    }

    @Override
    public void castSpell(Player p) {
        Collection<Entity> damagedEntities = p.getWorld().getNearbyEntities(p.getLocation(), 10, 0.5, 10);
        for (Entity e : damagedEntities) {
            if (e != p) {
                Vector flingDirection = p.getLocation().toVector().subtract(e.getLocation().toVector());
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
        for (int i = 0; i < 7; i++) {
            int size = i * 2;
            int points = i * 5 + 6;
            for (int j = 0; j < 360; j += 360/points) {
                double angle = (j * Math.PI / 180);
                double x = size * Math.cos(angle);
                double z = size * Math.sin(angle);
                Location loc = p.getLocation().add(x, 1, z);
                p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 1, 0, 0, 0, 0);
            }
        }
        BukkitUtils.scheduleRepeatingCancelTask(() -> {
            for (Entity e : damagedEntities) {
                if (e != p) {
                    e.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 6, 0.5, 0.5, 0.5);
                }
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
        }, 0, 1, 15);
    }
}
