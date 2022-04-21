package me.magikus.guis;

import me.magikus.core.gui.GuiManager;
import me.magikus.guis.spellSelect.SpellComboSelectionPage;
import me.magikus.guis.spellSelect.SpellMainPage;
import me.magikus.guis.spellSelect.SpellSelectionPage;

public class GuiRegister {

    public static void registerGuis() {
        GuiManager.registerGui("spells", new SpellMainPage());
        GuiManager.registerGui("spellselect", new SpellSelectionPage());
        GuiManager.registerGui("spellcomboselect", new SpellComboSelectionPage());
    }

}
