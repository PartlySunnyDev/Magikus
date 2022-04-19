package me.magikus.entities.behaviour.abilities;

import me.magikus.Magikus;
import me.magikus.core.entities.behaviour.abilities.EntityAbility;
import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.damage.modifiers.DamageModifier;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.tools.classes.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static me.magikus.core.entities.damage.DamageManager.getHitDamageOn;

public class FireStormEntityAbility extends EntityAbility {
    public FireStormEntityAbility() {
        super("firestorm");
    }

    @Override
    protected int getCastFrequency() {
        return 300;
    }

    @Override
    protected boolean doesShowParticlesBeforeCast() {
        return true;
    }

    @Override
    protected int shownParticlesTime() {
        return 40;
    }

    @Override
    protected Consumer<Entity> getAction() {
        return entity -> {
            Location center = entity.getLocation();
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            BukkitTask t = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                List<Entity> damagedEntities = List.copyOf(entity.getWorld().getNearbyEntities(center, 16, 0.5, 16));
                for (Entity e : damagedEntities) {
                    if (e instanceof Player p) {
                        DamageManager.damagePlayer(p, getHitDamageOn(p, EntityStatType.getStat(entity, EntityStatType.DAMAGE), false), EntityDamageEvent.DamageCause.FIRE, "Fire Storm");
                        p.damage(0);
                    } else if (e instanceof LivingEntity) {
                        DamageManager.dealDamage((LivingEntity) e, new HashMap<>() {{
                            put(Element.FIRE, 50d);
                        }}, new Pair<>(50d, true), new DamageModifier[]{DamageModifier.NORMAL}, false, DamageType.PHYSICAL);
                    }
                }
                for (int i = 0; i < 32; i++) {
                    for (int j = 0; j < 32; j++) {
                        entity.getWorld().spawnParticle(Particle.FALLING_LAVA, center.getX() + (i - 16), center.getY(), center.getZ() + (j - 16), 2, 0.5, 0.5, 0.5);
                        entity.getWorld().spawnParticle(Particle.DRIP_LAVA, center.getX() + i - 16, center.getY(), center.getZ() + (j - 16), 3, 0.5, 0.5, 0.5);
                    }
                }
            }, 0, 5);
            Bukkit.getScheduler().runTaskLater(plugin, t::cancel, 160);
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
        return 40;
    }
}
