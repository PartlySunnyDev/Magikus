package me.magikus.entities;

import me.magikus.core.entities.behaviour.abilities.EntityAbilityList;
import me.magikus.core.entities.behaviour.abilities.EntityAbilityManager;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.util.EntityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import org.bukkit.entity.Mob;

public class SuperSkeleton extends Skeleton {
    public SuperSkeleton(EntityType<? extends Skeleton> entitytypes, Level world) {
        super(entitytypes, world);
        this.fireImmune();
        EntityUtils.setEntityInfo(getBukkitEntity(), "super_skeleton");
    }

    @Override
    public void tick() {
        super.tick();
        this.setRemainingFireTicks(0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new FloatGoal(this));
        new EntityAbilityList(
                new Pair<>(EntityAbilityManager.getRegisteredAbility("push"), 5),
                new Pair<>(EntityAbilityManager.getRegisteredAbility("firestorm"), 1)
        ).applyGoal(((Mob) getBukkitEntity()), goalSelector, 1);
    }
}
