package me.magikus.core.commands;

import me.magikus.core.gui.GuiManager;
import me.magikus.guis.GuiRegister;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagikusGui implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("mgui")) {
            return true;
        }
        String guiId = strings[0];
        GuiManager.setInventory(p, GuiRegister.findGui(guiId));
        return true;
    }
}
