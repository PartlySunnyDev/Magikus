package me.magikus.entities;

import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStat;
import me.magikus.core.entities.stats.EntityStatSet;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityRegister {

    public static void registerEntityInfos() {
        EntityManager.addEntity(new EntityInfo("super_zombie", "Super Zombie Defender", ChatColor.GOLD + "" + ChatColor.BOLD, 5, true, new EntityStatSet(
                new EntityStat(EntityStatType.MAX_HEALTH, 100000),
                new EntityStat(EntityStatType.DEFENSE, 300),
                new EntityStat(EntityStatType.SPEED, 200),
                new EntityStat(EntityStatType.DAMAGE, 300)), SuperZombie.class, new ItemStack[]{
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_BOOTS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_LEGGINGS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_CHESTPLATE),
                ItemUtils.getSkullItem("f2b6fc2e-0c47-4b19-a21f-315877a2fe26", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0MjhhMTQxYTM5ODNjZGM5ZjgyMWYzNDc5M2Q5MmZhYWU3YjA4NWQyNjAwODM4NDc4NzA1OGUyZDkzZTM1MCJ9fX0="),
        }, Element.EARTH
        ));
        EntityManager.addEntity(new EntityInfo("ultra_zombie", "Ultra Zombie", ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD, 5, true, new EntityStatSet(
                new EntityStat(EntityStatType.MAX_HEALTH, 10000000),
                new EntityStat(EntityStatType.DEFENSE, 300),
                new EntityStat(EntityStatType.SPEED, 200),
                new EntityStat(EntityStatType.DAMAGE, 800)), UltraZombie.class, new ItemStack[]{
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_BOOTS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_LEGGINGS),
                ItemUtils.getLeatherArmorItem(Color.fromRGB(100, 20, 20), Material.LEATHER_CHESTPLATE),
                ItemUtils.getSkullItem("f2b6fc2e-0c47-4b19-a21f-315877a2fe26", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE0MjhhMTQxYTM5ODNjZGM5ZjgyMWYzNDc5M2Q5MmZhYWU3YjA4NWQyNjAwODM4NDc4NzA1OGUyZDkzZTM1MCJ9fX0="),
        }, Element.EARTH
        ));
    }

}
