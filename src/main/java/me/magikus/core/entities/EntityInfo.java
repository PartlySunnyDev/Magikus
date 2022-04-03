package me.magikus.core.entities;

import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStatSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public record EntityInfo(String id, String displayName, String color, int level, boolean isBoss,
                         EntityStatSet stats, Class<?> entityClass, /*REMEMBER THIS IS IN REVERSE (BOOTS TO HELMET)*/
                         ItemStack[] armorSlots, Element[] types) {

    public static Entity getEntity(EntityInfo info, Level world) {
        try {
            return (Entity) info.entityClass.getDeclaredConstructor(Level.class).newInstance(world);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException ignored) {
        }
        return null;
    }

}
