package me.magikus.core.generation.biomes.spawning;

import me.magikus.core.entities.EntityManager;
import me.magikus.core.util.EntityUtils;
import me.magikus.core.util.RandomList;
import me.magikus.core.util.classes.Triplet;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class EntitySpawnManager implements Listener {

    private static final Map<Biome, Map<EntityType<?>, Map<Class<? extends LivingEntity>, Integer>>> customBiomeSpawns = new HashMap<>();

    @SafeVarargs
    public static void registerSettings(Biome biome, Triplet<Weight, Class<? extends LivingEntity>, EntityType<?>>... data) {
        customBiomeSpawns.put(biome, new HashMap<>());
        for (Triplet<Weight, Class<? extends LivingEntity>, EntityType<?>> a : data) {
            Map<Class<? extends LivingEntity>, Integer> replacementValues = customBiomeSpawns.get(biome).get(a.c());
            if (replacementValues == null) {
                customBiomeSpawns.get(biome).put(a.c(), new HashMap<>());
                replacementValues = customBiomeSpawns.get(biome).get(a.c());
            }
            replacementValues.put(a.b(), a.a().asInt());
        }
    }

    public static Entity getPossibleReplacement(Biome biome, Entity e) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!customBiomeSpawns.containsKey(biome)) {
            return e;
        }
        Map<EntityType<?>, Map<Class<? extends LivingEntity>, Integer>> spawnInfo = customBiomeSpawns.get(biome);
        RandomList<Class<? extends LivingEntity>> selectedList = new RandomList<>();
        Map<Class<? extends LivingEntity>, Integer> classIntegerMap = spawnInfo.get(e.getType());
        if (classIntegerMap == null) {
            return e;
        }
        for (Class<? extends LivingEntity> clazz : classIntegerMap.keySet()) {
            selectedList.add(clazz, classIntegerMap.get(clazz));
        }
        if (selectedList.isEmpty()) {
            return e;
        }
        Class<? extends LivingEntity> raffle = selectedList.raffle();
        return raffle.getDeclaredConstructor(EntityType.class, Level.class).newInstance(EntityManager.getEntity(raffle).entityType(), e.getLevel());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void entitySpawnReplace(CreatureSpawnEvent e) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Biome b = ((CraftEntity) e.getEntity()).getHandle().getLevel().getBiome(((CraftEntity) e.getEntity()).getHandle().getOnPos()).value();
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM || !customBiomeSpawns.containsKey(b)) {
            return;
        }
        Entity entity = getPossibleReplacement(b, ((CraftEntity) (e.getEntity())).getHandle());
        Location l = e.getLocation();
        entity.setPos(l.getX(), l.getY(), l.getZ());
        e.getEntity().teleport(new Location(e.getLocation().getWorld(), e.getLocation().getX(), -100, e.getLocation().getZ()));
        e.getEntity().remove();
        EntityUtils.spawnEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

}
