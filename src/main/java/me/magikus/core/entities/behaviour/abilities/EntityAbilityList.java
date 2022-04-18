package me.magikus.core.entities.behaviour.abilities;

import me.magikus.core.items.abilities.Ability;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.classes.RandomList;
import me.magikus.core.tools.util.NumberUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.bukkit.entity.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityAbilityList {
    private final RandomList<EntityAbility> abilities = new RandomList<>();
    private final Map<String, Integer> abilityCooldowns = new HashMap<>();

    @SafeVarargs
    public EntityAbilityList(Pair<EntityAbility, Integer>... abilities) {
        for (Pair<EntityAbility, Integer> a : abilities) {
            this.abilities.add(a.a(), a.b());
        }
    }

    public void addAbility(EntityAbility a, Integer weight) {
        abilities.add(a, weight);
    }

    public void removeAbility(EntityAbility a) {
        abilities.remove(a);
    }

    public EntityAbility getAbility() {
        if (abilityCooldowns.size() >= abilities.size()) {
            return null;
        }
        EntityAbility raffle = abilities.raffle();
        return abilityCooldowns.containsKey(raffle.id()) ? getAbility() : raffle;
    }

    public void resetCooldown(EntityAbility a) {
        abilityCooldowns.put(a.id(), (int) NumberUtils.randomBetween(a.getCastFrequency() - a.getCastFrequency() / 20f, a.getCastFrequency() + a.getCastFrequency() / 20f));
    }

    public void tick() {
        List<String> remove = new ArrayList<>();
        abilityCooldowns.replaceAll((s, v) -> {
            if (v < 1) {
                remove.add(s);
            }
            return v - 1;
        });
        for (String s : remove) {
            abilityCooldowns.remove(s);
        }
    }

    public void applyGoal(Mob m, GoalSelector gs, int priority) {
        gs.addGoal(priority, new EntityAbilityCastGoal(m, this));
    }



}
