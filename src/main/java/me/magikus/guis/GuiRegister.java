package me.magikus.guis;

import me.magikus.core.gui.GuiManager;
import me.magikus.core.gui.MagikusGui;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiRegister {

    public static void registerGuis() {
        GuiManager.registerGui("testgui", TestGui.class);
    }

}
