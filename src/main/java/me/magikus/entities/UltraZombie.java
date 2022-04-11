package me.magikus.entities;

import me.magikus.core.util.EntityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class UltraZombie extends Zombie {
    public UltraZombie(EntityType<? extends Zombie> entitytypes, Level world) {
        super(entitytypes, world);
        this.fireImmune();
        EntityUtils.setEntityInfo(getBukkitEntity(), "ultra_zombie");
    }

    @Override
    public void tick() {
        super.tick();
        this.setRemainingFireTicks(0);
    }
}
