package me.magikus.core.entities.behaviour.abilities;

import me.magikus.core.tools.util.NumberUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityAbilityCastGoal extends Goal {

    private final Entity parent;
    private final EntityAbilityList l;
    private EntityAbility currentlyCasting;
    private int frequencyCounter = -1;
    private boolean particlePhase = false;
    private int particlePhaseCounter = -1;

    public EntityAbilityCastGoal(Entity parent, EntityAbilityList l) {
        this.parent = parent;
        this.l = l;
    }

    @Override
    public void tick() {
        l.tick();
        if (currentlyCasting == null) {
            EntityAbility newSpell = l.getAbility();
            if (newSpell == null) {
                return;
            }
            l.resetCooldown(newSpell);
            currentlyCasting = newSpell;
        }
        if (frequencyCounter <= 0 && currentlyCasting.getPredicate().test(parent) && !particlePhase) {
            frequencyCounter = -1;
            if (currentlyCasting.doesShowParticlesBeforeCast()) {
                particlePhaseCounter = currentlyCasting.shownParticlesTime();
                particlePhase = true;
            } else {
                currentlyCasting.getAction().accept(parent);
                frequencyCounter = currentlyCasting.delayTime();
                currentlyCasting = null;
            }
        } else if (!particlePhase) {
            frequencyCounter--;
        }
        if (particlePhase) {
            particlePhaseCounter--;
            parent.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, parent.getLocation(), 5, 0.7, 1, 0.7);
            if (particlePhaseCounter <= 0) {
                particlePhaseCounter = -1;
                particlePhase = false;
                frequencyCounter = currentlyCasting.delayTime();
                if (currentlyCasting.getPredicate().test(parent)) {
                    currentlyCasting.getAction().accept(parent);
                }
                currentlyCasting = null;
            }
        }
    }

    @Override
    public boolean canUse() {
        return true;
    }



}
