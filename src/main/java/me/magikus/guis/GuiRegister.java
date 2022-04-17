package me.magikus.guis;

import me.magikus.core.gui.MagikusGui;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiRegister {

    private static final Map<String, Class<? extends MagikusGui>> guis = new HashMap<>();

    public static void registerGuis() {
        guis.put("testgui", TestGui.class);
    }

    @Nullable
    public static MagikusGui createGui(String id) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends MagikusGui> aClass = guis.get(id);
        if (aClass == null) {
            return null;
        }
        return aClass.getDeclaredConstructor().newInstance();
    }

}
