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
import java.util.Objects;

public class SpellMainPage implements GuiInstance {

    @Override
    public Gui getGui(HumanEntity e) {
        if (!(e instanceof Player player)) return new ChestGui(5, "");
        ChestGui gui = new ChestGui(5, StringHolder.of(ChatColor.BLUE + "Your Spells"));
        StaticPane p = new StaticPane(0, 0, 9, 9);
        gui.setOnGlobalClick(event -> {
            if (event.getWhoClicked() instanceof Player a) {
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
            String currentComboInSlot = SpellPreferences.getComboForSlot(player, count);
            if (currentComboInSlot == null || Objects.equals(currentComboInSlot, "")) {
                currentComboInSlot = "None";
            }
            if (targetSpell == null) {
                p.addItem(new GuiItem(ItemBuilder.builder(Material.BARRIER).setName(ChatColor.RED + "No spell selected!").setLore(ChatColor.GRAY + "Select a spell with the button below!").build()), index, 2);
            } else {
                List<String> lore = new ArrayList<>(TextUtils.wrap(targetSpell.description(), 30, org.bukkit.ChatColor.GRAY));
                lore.add(ChatColor.DARK_GRAY + "Mana cost: " + StatType.MANA.color() + targetSpell.manaCost() + "" + StatType.MANA.symbol());
                p.addItem(new GuiItem(ItemBuilder.builder(Material.HEART_OF_THE_SEA).setName(targetSpell.displayName() + ChatColor.BLUE + " Spell").setLore(lore.toArray(new String[0])).build()), index, 2);
            }
            int finalCount = count;
            p.addItem(new GuiItem(ItemBuilder.builder(Material.SUGAR).setName(ChatColor.GREEN + "Change Spell").build(), event -> {
                player.closeInventory();
                SpellSelectionPage.setCurrentlyChanging(player, finalCount);
                GuiManager.setInventory(player, "spellselect");
            }), index, 3);
            p.addItem(new GuiItem(ItemBuilder.builder(Material.STICK).setName(ChatColor.GREEN + "Change Combo").setLore(ChatColor.GRAY + "Current combo: " + currentComboInSlot).build(), event -> {
                player.closeInventory();
                SpellComboSelectionPage.setCurrentlyChanging(player, finalCount);
                GuiManager.setInventory(player, "spellcomboselect");
            }), index, 1);
            count++;
        }
        gui.addPane(p);
        return gui;
    }
}
