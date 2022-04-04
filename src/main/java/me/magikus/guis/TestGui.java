package me.magikus.guis;

import me.magikus.core.gui.MagikusGui;
import me.magikus.core.gui.components.DecorComponent;
import org.bukkit.Material;

public class TestGui extends MagikusGui {

    protected TestGui() {
        super("test", 9, "Test GUI");
    }

    @Override
    protected void buildGui() {
        for (int i = 0; i < 9; i++) {
            contents.add(new DecorComponent(Material.DIAMOND, this));
        }
    }

}
