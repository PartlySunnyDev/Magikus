package me.magikus.core.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.additions.enchants.Enchant;
import me.magikus.core.items.additions.enchants.EnchantManager;
import me.magikus.core.util.DataUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagikusEnchant implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("menchant")) {
            return true;
        }
        String enchantId = strings[0];
        int level = 1;
        if (strings.length > 1) {
            try {
                level = Integer.parseInt(strings[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        Enchant info = EnchantManager.getEnchant(enchantId);
        if (info == null) {
            p.sendMessage(ChatColor.RED + "Invalid enchant type: " + enchantId);
            return true;
        }
        ItemStack stack = p.getInventory().getItemInMainHand();
        MagikusItem mgi;
        if (new NBTItem(stack).getBoolean("vanilla")) {
            mgi = MagikusItem.getItemFrom(stack, p);
        } else {
            mgi = DataUtils.getSkyblockItem(stack, p);
        }
        if (mgi == null) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not valid: " + stack.getType());
            return true;
        }
        if (!info.canApply(mgi)) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not compatible with this enchant!");
            return true;
        }
        /*
        if (level > info.maxLevel() || (level == mgi.enchants().getLevelOf(enchantId) && level + 1 > info.maxLevel())) {
            p.sendMessage(ChatColor.RED + "The enchant level is too high!");
            return true;
        }
         */
        mgi.enchants().addEnchant(info.id(), level);
        p.getInventory().setItemInMainHand(mgi.getSkyblockItem());
        p.sendMessage(ChatColor.GREEN + "Successfully applied the enchant " + info.id() + " of level " + level + " to your held item");
        return true;
    }
}
