package me.magikus.guis;

import me.magikus.core.gui.GuiManager;

public class GuiRegister {

    public static void registerGuis() {
        GuiManager.registerGui("testgui", TestGui.class);
    }

}
