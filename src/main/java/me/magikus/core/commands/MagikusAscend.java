package me.magikus.core.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.items.additions.ascensions.AscensionManager;
import me.magikus.core.util.DataUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MagikusAscend implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("mascend")) {
            return true;
        }
        String ascensionId = strings[0];
        Ascension info = AscensionManager.getAscension(ascensionId);
        if (info == null) {
            p.sendMessage(ChatColor.RED + "Invalid ascension type: " + ascensionId);
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
        if (!mgi.type().reforgable()) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not reforgable!");
            return true;
        }
        if (!info.canApply(mgi)) {
            p.sendMessage(ChatColor.RED + "Item on your hand is not compatible with this ascension!");
            return true;
        }
        mgi.setAscension(info.id());
        p.getInventory().setItemInMainHand(mgi.getSkyblockItem());
        p.sendMessage(ChatColor.GREEN + "Successfully applied the ascension " + info.id() + " to your held item");
        return true;
    }
}
