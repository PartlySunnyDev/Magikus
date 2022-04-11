package me.magikus.core.util;

import me.magikus.core.ConsoleLogger;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import static me.magikus.core.util.ObfField.FROZEN;
import static me.magikus.core.util.ObfField.INTRUSIVE_HOLDER_CACHE;

public class RegistryUtils {

    public static Field findByType(Class<?> owner, Class<?> fieldType) throws Exception {
        Field[] fields = owner.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == fieldType) return field;
        }
        return null;
    }

    public static Field findMapByType(Class<?> owner, Class<?> keyType, Class<?> valueType) {
        Field[] fields = owner.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == Map.class) {
                if (field.getType().getTypeParameters()[0].getClass() == keyType.getGenericSuperclass().getClass()) {
                    if (field.getType().getTypeParameters()[1].getClass() == valueType.getGenericSuperclass().getClass()) {
                        return field;
                    }
                }
            }
        }
        return null;
    }

    public static <E extends Registry<?>> E registry(ResourceKey<E> key) {
        return (E) Registry.REGISTRY.get((ResourceKey) key);
    }

    public static void unfreezeEntityRegistry() {
        Class<MappedRegistry> registryClass = MappedRegistry.class;
        try {
            Field intrusiveHolderCache = registryClass.getDeclaredField(INTRUSIVE_HOLDER_CACHE);
            intrusiveHolderCache.setAccessible(true);
            intrusiveHolderCache.set(Registry.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
            Field frozen = registryClass.getDeclaredField(FROZEN);
            frozen.setAccessible(true);
            frozen.set(Registry.ENTITY_TYPE, false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static AttributeInstance getAttributeGiveOrCreate(LivingEntity e, Attribute a) throws Exception {
        AttributeMap attributes = e.getAttributes();
        ConsoleLogger.console(String.valueOf(attributes));
        Field f = findByType(AttributeMap.class, AttributeSupplier.class);
        f.setAccessible(true);
        AttributeSupplier as = (AttributeSupplier) f.get(attributes);
        f.setAccessible(false);
        if (as == null) {
            attributes.assignValues(new AttributeMap(AttributeSupplier.builder().build()));
        }
        return attributes.getInstance(a);
    }

    public static void unfreezeBiomeRegistry() {
        Server server = Bukkit.getServer();
        CraftServer craftserver = (CraftServer) server;
        DedicatedServer dedicatedserver = craftserver.getServer();
        Class<MappedRegistry> registryClass = MappedRegistry.class;
        try {
            Field intrusiveHolderCache = registryClass.getDeclaredField(INTRUSIVE_HOLDER_CACHE);
            intrusiveHolderCache.setAccessible(true);
            intrusiveHolderCache.set(dedicatedserver.registryAccess().registry(Registry.BIOME_REGISTRY).get(), new IdentityHashMap<Biome, Holder.Reference<Biome>>());
            Field frozen = registryClass.getDeclaredField(FROZEN);
            frozen.setAccessible(true);
            frozen.set(dedicatedserver.registryAccess().registry(Registry.BIOME_REGISTRY).get(), false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
