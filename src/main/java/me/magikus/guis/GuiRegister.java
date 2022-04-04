package me.magikus.guis;

import me.magikus.core.gui.MagikusGui;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GuiRegister {

    private static final List<MagikusGui> guis = new ArrayList<>();

    public static void registerGuis() {
        guis.add(new TestGui());
    }

    @Nullable
    public static MagikusGui findGui(String id) {
        return guis.stream().filter((magikusGui -> magikusGui.id().equalsIgnoreCase(id))).findFirst().orElse(null);
    }

}
