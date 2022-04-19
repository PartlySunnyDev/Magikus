package me.magikus.core.entities.behaviour.abilities;

import org.bukkit.entity.Entity;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class EntityAbility {

    private final String id;

    public EntityAbility(String id) {
        this.id = id;
    }

    //About how many ticks in between every cast
    protected abstract int getCastFrequency();

    //Warning particles before cast
    protected abstract boolean doesShowParticlesBeforeCast();

    //Total time of warning
    protected abstract int shownParticlesTime();

    protected abstract Consumer<Entity> getAction();

    protected abstract Predicate<Entity> getPredicate();

    protected abstract int delayTime();

    public String id() {
        return id;
    }
}
