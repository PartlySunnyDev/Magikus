package me.magikus.entities;

import me.magikus.core.util.EntityUtils;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class SuperZombie extends Zombie {
    public SuperZombie(Level world) {
        super(world);
        EntityUtils.setEntityInfo(getBukkitEntity(), "super_zombie");
    }
}
