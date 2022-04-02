package me.magikus.core.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.util.DataUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagikusFrag implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || !s.equals("sbfrag")) {
            return true;
        }
        String bool;
        if (strings.length > 0) {
            bool = strings[0];
        } else {
            bool = "true";
        }
        boolean frag = false;
        if (bool.equals("0") || bool.equals("true")) {
            frag = true;
        } else if (bool.equals("1") || bool.equals("false")) {
            frag = false;
        } else {
            p.sendMessage(ChatColor.RED + "Please enter true or false.");
            return true;
        }
        ItemStack stack = p.getInventory().getItemInMainHand();
        MagikusItem sbi;
        if (new NBTItem(stack).getBoolean("vanilla")) {
            sbi = MagikusItem.getItemFrom(stack, p);
        } else {
            sbi = DataUtils.getSkyblockItem(stack, p);
        }
        if (sbi == null) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not valid: " + stack.getType());
            return true;
        }
        sbi.setFragged(frag);
        p.getInventory().setItemInMainHand(sbi.getSkyblockItem());
        p.sendMessage(ChatColor.GREEN + "Successfully fragged / defragged your item");
        return true;
    }
}
