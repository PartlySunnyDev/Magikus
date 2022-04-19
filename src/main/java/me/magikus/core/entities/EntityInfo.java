package me.magikus.core.entities;

import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStatList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public record EntityInfo(String id, String displayName, String color, int level, boolean isBoss,
                         EntityStatList stats, Class<?> entityClass, /*REMEMBER THIS IS IN REVERSE (BOOTS TO HELMET)*/
                         ItemStack[] armorSlots, ItemStack itemInMainHand, Element type, EntityType<?> entityType) {

    public static Entity getEntity(EntityInfo info, Level world) {
        try {
            return (Entity) info.entityClass.getDeclaredConstructor(EntityType.class, Level.class).newInstance(info.entityType, world);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
