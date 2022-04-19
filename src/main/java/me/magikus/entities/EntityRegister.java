package me.magikus.entities;

import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.behaviour.abilities.EntityAbilityManager;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStat;
import me.magikus.core.entities.stats.EntityStatList;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.tools.util.ItemUtils;
import me.magikus.entities.behaviour.abilities.ExplosiveChargeEntityAbility;
import me.magikus.entities.behaviour.abilities.FireStormEntityAbility;
import me.magikus.entities.behaviour.abilities.PullAbility;
import me.magikus.entities.behaviour.abilities.PushAbility;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.magikus.core.tools.util.RegistryUtils.unfreezeEntityRegistry;

public class EntityRegister {

    public static void registerEntityAbilities() {
        EntityAbilityManager.registerEntityAbility(new ExplosiveChargeEntityAbility());
        EntityAbilityManager.registerEntityAbility(new FireStormEntityAbility());
        EntityAbilityManager.registerEntityAbility(new PushAbility());
        EntityAbilityManager.registerEntityAbility(new PullAbility());
    }

    public static void registerEntityInfos() throws NoSuchFieldException, IllegalAccessException {
        unfreezeEntityRegistry();
        EntityManager.addEntity(new EntityInfo("super_zombie", "Super Zombie Defender", ChatColor.GOLD + "" + ChatColor.BOLD, 5, true, new EntityStatList(
                new EntityStat(EntityStatType.MAX_HEALTH, 100000),
                new EntityStat(EntityStatType.ELECTRIC_DEFENSE, 1000000),
                new EntityStat(EntityStatType.FIRE_DEFENSE, -1000000),
                new EntityStat(EntityStatType.SPEED, 200),
                new EntityStat(EntityStatType.ELECTRIC_DAMAGE, 100)), SuperZombie.class, new ItemStack[]{
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_BOOTS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_LEGGINGS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_CHESTPLATE),
                ItemUtils.getSkullItem("f2b6fc2e-0c47-4b19-a21f-315877a2fe26", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0MjhhMTQxYTM5ODNjZGM5ZjgyMWYzNDc5M2Q5MmZhYWU3YjA4NWQyNjAwODM4NDc4NzA1OGUyZDkzZTM1MCJ9fX0="),
        }, new ItemStack(Material.DIAMOND_AXE), Element.ELECTRIC, EntityType.ZOMBIE
        ));
        EntityManager.addEntity(new EntityInfo("ultra_zombie", "Ultra Zombie", ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD, 5, true, new EntityStatList(
                new EntityStat(EntityStatType.MAX_HEALTH, 10000000),
                new EntityStat(EntityStatType.FIRE_DEFENSE, 1000000),
                new EntityStat(EntityStatType.ELECTRIC_DEFENSE, -1000000),
                new EntityStat(EntityStatType.FIRE_DAMAGE, 100),
                new EntityStat(EntityStatType.SPEED, 200)), UltraZombie.class, new ItemStack[]{
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_BOOTS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_LEGGINGS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_CHESTPLATE),
                ItemUtils.getSkullItem("f2b6fc2e-0c47-4b19-a21f-315877a2fe26", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0MjhhMTQxYTM5ODNjZGM5ZjgyMWYzNDc5M2Q5MmZhYWU3YjA4NWQyNjAwODM4NDc4NzA1OGUyZDkzZTM1MCJ9fX0="),
        }, new ItemStack(Material.NETHERITE_AXE), Element.FIRE, EntityType.ZOMBIE
        ));
        EntityManager.addEntity(new EntityInfo("super_skeleton", "SuperSkel-20F2", ChatColor.RED + "", 95, true, new EntityStatList(
                new EntityStat(EntityStatType.MAX_HEALTH, 150000000),
                new EntityStat(EntityStatType.FIRE_DEFENSE, 2500),
                new EntityStat(EntityStatType.ELECTRIC_DEFENSE, 4500),
                new EntityStat(EntityStatType.WIND_DEFENSE, 2500),
                new EntityStat(EntityStatType.WATER_DEFENSE, -2500),
                new EntityStat(EntityStatType.EARTH_DEFENSE, -2500),
                new EntityStat(EntityStatType.FIRE_DAMAGE, 1000),
                new EntityStat(EntityStatType.ELECTRIC_DAMAGE, 2000),
                new EntityStat(EntityStatType.SPEED, 300)), SuperSkeleton.class, new ItemStack[]{
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_BOOTS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_LEGGINGS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_CHESTPLATE),
                null,
        }, new ItemStack(Material.BOW), Element.ELECTRIC, EntityType.SKELETON
        ));
        Registry.ENTITY_TYPE.freeze();
    }

}
