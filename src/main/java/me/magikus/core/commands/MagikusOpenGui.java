package me.magikus.core.commands;

import me.magikus.core.gui.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class MagikusOpenGui implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length < 1 || !s.equals("mgui")) {
            return true;
        }
        String guiId = strings[0];
        me.magikus.core.gui.MagikusGui gui;
        try {
            gui = GuiManager.createGui(guiId);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (gui == null) {
            return true;
        }
        GuiManager.setInventory(p, gui);
        return true;
    }
}
