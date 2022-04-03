package me.magikus.entities;

import me.magikus.core.util.EntityUtils;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class UltraZombie extends Zombie {
    public UltraZombie(Level world) {
        super(world);
        EntityUtils.setEntityInfo(getBukkitEntity(), "ultra_zombie");
    }
}
