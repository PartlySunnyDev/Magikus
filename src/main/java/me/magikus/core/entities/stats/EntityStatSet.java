package me.magikus.core.entities.stats;

import org.bukkit.entity.Entity;

import static me.magikus.core.entities.stats.EntityStatType.setStat;

public record EntityStatSet(EntityStat maxHealth,
                            EntityStat defense,
                            EntityStat speed,
                            EntityStat damage) {

    public EntityStatSet {
        if (
            maxHealth.type() != EntityStatType.MAX_HEALTH ||
                defense.type() != EntityStatType.DEFENSE ||
                damage.type() != EntityStatType.DAMAGE ||
                speed.type() != EntityStatType.SPEED
        ) {
            throw new IllegalArgumentException("Wrong entity stat type in set!");
        }
    }

    public void apply(Entity entity) {
        setStat(entity, EntityStatType.MAX_HEALTH, maxHealth.value());
        setStat(entity, EntityStatType.DEFENSE, defense.value());
        setStat(entity, EntityStatType.DAMAGE, damage.value());
        setStat(entity, EntityStatType.SPEED, speed.value());
    }

}
