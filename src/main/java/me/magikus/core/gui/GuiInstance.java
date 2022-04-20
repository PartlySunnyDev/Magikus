package me.magikus.core.gui;

import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public interface GuiInstance {
    Gui getGui(HumanEntity p);
    default void openFor(HumanEntity e) {
        getGui(e).show(e);
    }
}
