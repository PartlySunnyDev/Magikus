package me.magikus.entities;

import me.magikus.core.entities.behaviour.abilities.EntityAbilityList;
import me.magikus.core.entities.behaviour.abilities.EntityAbilityManager;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.util.EntityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.entity.Mob;

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

    @Override
    protected void registerGoals() {
        super.registerGoals();
        new EntityAbilityList(
                new Pair<>(EntityAbilityManager.getRegisteredAbility("firestorm"), 1),
                new Pair<>(EntityAbilityManager.getRegisteredAbility("explosive_charge"), 1)
        ).applyGoal(((Mob) getBukkitEntity()), goalSelector, 1);
    }
}
