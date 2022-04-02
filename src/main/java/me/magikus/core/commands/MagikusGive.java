package me.magikus.core.commands;

import me.magikus.core.items.ItemInfo;
import me.magikus.core.items.ItemManager;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagikusGive implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("sbgive")) {
            return true;
        }
        String itemId = strings[0];
        ItemInfo infoFromId = ItemManager.getInfoFromId(itemId);
        if (infoFromId == null) {
            p.sendMessage(ChatColor.RED + "Invalid item type: " + strings[0]);
            return true;
        }
        MagikusItem instance = ItemManager.getInstance(infoFromId, p);
        if (instance != null) {
            instance.updateSkyblockItem();
            if (strings.length > 1) {
                try {
                    instance.setStackCount(Integer.parseInt(strings[1]));
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Invalid number provided. Giving one item instead.");
                }
            }
            ItemUtils.addItem(p, instance.getSkyblockItem());
            p.sendMessage(ChatColor.GREEN + "Gave you " + instance.stackCount() + " " + instance.getDisplayName());
        } else {
            p.sendMessage(ChatColor.RED + "Invalid item type: " + strings[0]);
        }
        return true;
    }
}
