package me.magikus.guis.spellSelect;

import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.magikus.core.gui.GuiInstance;
import me.magikus.core.gui.GuiManager;
import me.magikus.core.magic.spells.Spell;
import me.magikus.core.magic.spells.SpellManager;
import me.magikus.core.magic.spells.SpellPreferences;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.classes.ItemBuilder;
import me.magikus.core.tools.util.NumberUtils;
import me.magikus.core.tools.util.TextUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpellMainPage implements GuiInstance {

    @Override
    public Gui getGui(HumanEntity e) {
        if (!(e instanceof Player player)) return new ChestGui(4, "");
        ChestGui gui = new ChestGui(4, StringHolder.of(ChatColor.BLUE + "Your Spells"));
        StaticPane p = new StaticPane(0, 0, 9, 4);
        gui.setOnGlobalClick(event -> {
            if (event.getWhoClicked() instanceof Player a){
                a.playSound(a.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, 1, 1);
            }
            event.setCancelled(true);
        });
        p.fillWith(ItemBuilder.builder(Material.GRAY_STAINED_GLASS_PANE).setName("").build(), event -> event.setCancelled(true));
        int numToDisplay = SpellPreferences.spellSlotsUnlocked(player);
        double[] linSpace = NumberUtils.linspace(1, 7, numToDisplay);
        int count = 0;
        for (double d : linSpace) {
            int index = (int) Math.round(d);
            Spell targetSpell = SpellManager.getRegisteredSpell(SpellPreferences.getSpellInSlot(player, count));
            if (targetSpell == null) {
                p.addItem(new GuiItem(ItemBuilder.builder(Material.BARRIER).setName(ChatColor.RED + "No spell selected!").setLore(ChatColor.GRAY + "Select a spell with the button below!").build()), index, 1);
            } else {
                List<String> lore = new ArrayList<>(TextUtils.wrap(targetSpell.description(), 30, org.bukkit.ChatColor.GRAY));
                lore.add(ChatColor.DARK_GRAY + "Mana cost: " + targetSpell.manaCost() + "" + StatType.MANA.symbol());
                p.addItem(new GuiItem(ItemBuilder.builder(Material.HEART_OF_THE_SEA).setName(targetSpell.displayName() + ChatColor.BLUE + " Spell").setLore(lore.toArray(new String[0])).build()), index, 1);
            }
            int finalCount = count;
            p.addItem(new GuiItem(ItemBuilder.builder(Material.GREEN_CONCRETE).setName(ChatColor.BLUE + "Change Spell").build(), event -> {
                player.closeInventory();
                SpellSelectionPage.setCurrentlyChanging(player, finalCount);
                GuiManager.setInventory(player, "spellselect");
            }), index, 2);
            count++;
        }
        gui.addPane(p);
        return gui;
    }
}
