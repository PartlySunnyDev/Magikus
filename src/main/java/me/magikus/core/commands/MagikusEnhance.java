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

public class MagikusEnhance implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 0 || !s.equals("menhance")) {
            return true;
        }
        String number;
        if (strings.length > 0) {
            number = strings[0];
        } else {
            number = "5";
        }
        int enhancements;
        try {
            enhancements = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.RED + "Invalid number provided.");
            return true;
        }
        ItemStack stack = p.getInventory().getItemInMainHand();
        MagikusItem mgi;
        if (new NBTItem(stack).getBoolean("vanilla")) {
            mgi = MagikusItem.getItemFrom(stack, p);
        } else {
            mgi = DataUtils.getMagikusItem(stack, p);
        }
        if (mgi == null) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not valid: " + stack.getType());
            return true;
        }
        mgi.setEnhancements(enhancements);
        p.getInventory().setItemInMainHand(mgi.getMagikusItem());
        p.sendMessage(ChatColor.GREEN + "Successfully added " + enhancements + " enhancements to your item.");
        return true;
    }
}
