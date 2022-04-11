package me.magikus.core.generation.biomes;

import me.magikus.core.generation.biomes.spawning.EntitySpawnManager;
import me.magikus.core.util.classes.Triplet;
import me.magikus.entities.SuperZombie;
import me.magikus.entities.UltraZombie;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

public class BiomeRegister {

    public static void registerBiomeEntities() {
        EntitySpawnManager.registerSettings(getMagikusBiomeFromKey("stonelands"), new Triplet<>(Weight.of(1), UltraZombie.class, EntityType.ZOMBIE), new Triplet<>(Weight.of(5), SuperZombie.class, EntityType.ZOMBIE), new Triplet<>(Weight.of(1), UltraZombie.class, EntityType.PIG), new Triplet<>(Weight.of(5), SuperZombie.class, EntityType.PIG));
    }

    private static Biome getMagikusBiomeFromKey(String key) {
        return ((CraftServer) Bukkit.getServer()).getServer().registryAccess().registry(Registry.BIOME_REGISTRY).get().get(new ResourceLocation("magikus", key));
    }

}
