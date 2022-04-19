package me.magikus.entities;

import me.magikus.core.tools.util.EntityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class SuperZombie extends Zombie {
    public SuperZombie(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
        this.fireImmune();
        EntityUtils.setEntityInfo(getBukkitEntity(), "super_zombie");
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
    }
}
