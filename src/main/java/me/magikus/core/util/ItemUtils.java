package me.magikus.core.util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatBonus;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.UUID;

public class ItemUtils {

    public static NBTItem setItemStats(NBTItem item, Stat... stats) {
        for (Stat s : stats) {
            if (s.value() == 0) {
                item.removeKey(s.type().id());
                continue;
            }
            item.setDouble(s.type().id(), s.value());
        }
        return item;
    }

    public static NBTItem setItemBonus(NBTItem item, StatBonus... stats) {
        for (StatBonus s : stats) {
            if (s.bonus() == 1) {
                item.removeKey(s.t().id());
                continue;
            }
            item.setDouble(s.t().id(), s.bonus());
        }
        return item;
    }

    public static String getId(ItemStack i) {
        NBTItem nbti = new NBTItem(i);
        return nbti.getString("mg_id");
    }

    public static void setUnstackable(ItemStack i) {
        NBTItem nbti = new NBTItem(i);
        nbti.setUUID("unstack", UUID.randomUUID());
        nbti.applyNBT(i);
    }

    public static UUID getUniqueId(ItemStack i) {
        NBTItem nbti = new NBTItem(i);
        if (!nbti.hasKey("mg_unique_id")) {
            return null;
        }
        return nbti.getUUID("mg_unique_id");
    }

    public static void setUniqueId(ItemStack i, UUID id) {
        NBTItem nbti = new NBTItem(i);
        nbti.setUUID("mg_unique_id", id);
        nbti.applyNBT(i);
    }

    public static ItemStack getItemFrom(Material m, String name, String lore, boolean stackable) {
        ItemStack s = new ItemStack(m);
        ItemUtils.setNameAndLore(s, name, lore);
        if (!stackable) {
            ItemUtils.setUnstackable(s);
        }
        return s;
    }

    public static void addItem(Player player, ItemStack i) {
        if (i == null) {
            return;
        }
        if (player.getInventory().firstEmpty() == -1) {
            //TODO add part to existing ItemStacks
            player.getWorld().dropItem(player.getLocation(), i);
        } else {
            player.getInventory().addItem(i);
        }
    }

    public static ItemStack getSkullItem(String id, String value) {
        NBTItem nbti = new NBTItem(new ItemStack(Material.PLAYER_HEAD));
        NBTCompound skull = nbti.addCompound("SkullOwner");
        skull.setUUID("Id", UUID.fromString(id));

        NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
        texture.setString("Value", value);
        return nbti.getItem();
    }

    public static ItemStack getLeatherArmorItem(Color color, Material material) {
        ItemStack item = new ItemStack(material);
        if (item.getItemMeta() instanceof LeatherArmorMeta m) {
            m.setColor(color);
            item.setItemMeta(m);
        }
        return item;
    }

    public static void setNameAndLore(ItemStack i, String name, String lore) {
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);
        m.setLore(TextUtils.wrap(lore, 30, ChatColor.GRAY));
        i.setItemMeta(m);
    }

    public static void setId(ItemStack i, String id) {
        NBTItem nbti = new NBTItem(i);
        nbti.setString("mg_id", id);
        nbti.applyNBT(i);
    }
}
