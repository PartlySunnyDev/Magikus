package me.magikus.core.commands;

import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.util.EntityUtils;
import net.minecraft.world.entity.Entity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagikusSummon implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("msummon")) {
            return true;
        }
        String entityId = strings[0];
        EntityInfo info = EntityManager.getEntity(entityId);
        if (info == null) {
            p.sendMessage(ChatColor.RED + "Invalid entity type: " + strings[0]);
            return true;
        }
        Entity instance = EntityInfo.getEntity(info, ((CraftWorld) p.getWorld()).getHandle());
        if (instance != null) {
            int amount = 1;
            if (strings.length > 1) {
                try {
                    amount = Integer.parseInt(strings[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Invalid number provided. Summoning one entity instead.");
                }
            }
            for (int i = 0; i < amount; i++) {
                Entity entity = EntityInfo.getEntity(info, ((CraftWorld) p.getWorld()).getHandle());
                Location location = p.getLocation();
                entity.setPos(location.getX(), location.getY(), location.getZ());
                EntityUtils.spawnEntity(entity);
            }
            if (amount > 1) {
                p.sendMessage(ChatColor.GREEN + "Successfully summoned " + strings[1] + " " + info.color() + info.displayName());
            } else {
                p.sendMessage(ChatColor.GREEN + "Successfully summoned " + info.color() + info.displayName());
            }
        } else {
            p.sendMessage(ChatColor.RED + "Invalid entity type: " + strings[0]);
        }
        return true;
    }
}
