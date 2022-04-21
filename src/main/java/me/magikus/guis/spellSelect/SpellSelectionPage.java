package me.magikus.guis.spellSelect;

import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.magikus.core.gui.GuiInstance;
import me.magikus.core.gui.GuiManager;
import me.magikus.core.magic.spells.Spell;
import me.magikus.core.magic.spells.SpellManager;
import me.magikus.core.magic.spells.SpellPreferences;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.classes.ItemBuilder;
import me.magikus.core.tools.util.TextUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class SpellSelectionPage implements GuiInstance {

    private static final Map<UUID, Integer> currentlyChanging = new HashMap<>();

    public static void setCurrentlyChanging(Player p, Integer s) {
        currentlyChanging.put(p.getUniqueId(), s);
    }

    public static void removeCurrentlyChanging(Player p) {
        currentlyChanging.remove(p.getUniqueId());
    }

    @Nullable
    public static Integer getCurrentlyChanging(Player p) {
        return currentlyChanging.get(p.getUniqueId());
    }

    @Override
    public Gui getGui(HumanEntity e) {
        if (!(e instanceof Player player)) return new ChestGui(5, "");
        List<String> spells = SpellPreferences.getDiscoveredSpells(player);
        ChestGui gui = new ChestGui(5, StringHolder.of(ChatColor.BLUE + "Change Spell"));
        PaginatedPane pagPane = new PaginatedPane(0, 0, 9, 5);
        int numPages = (int) Math.ceil(spells.size() / 27f);
        if (numPages == 0) {
            StaticPane border = new StaticPane(0, 0, 9, 5);
            border.fillWith(ItemBuilder.builder(Material.BLACK_STAINED_GLASS_PANE).setName("").build(), event -> event.setCancelled(true));
            gui.setOnGlobalClick(event -> event.setCancelled(true));
            border.addItem(new GuiItem(ItemBuilder.builder(Material.ARROW).setName(ChatColor.RED + "Go Back").build(), event -> {
                player.closeInventory();
                removeCurrentlyChanging(player);
                GuiManager.setInventory(player, "spells");
            }), 0, 4);
            border.addItem(new GuiItem(ItemBuilder.builder(Material.BARRIER).setName(ChatColor.RED + "You have no spells!").setLore(TextUtils.wrap("Get spells by consuming spell orbs which you can craft or find around the world!", 30, org.bukkit.ChatColor.GRAY).toArray(new String[0])).build()), 4, 2);
            gui.addPane(border);
            return gui;
        }
        int count = 0;
        for (int i = 0; i < numPages; i++) {
            StaticPane border = new StaticPane(0, 0, 9, 5);
            border.fillWith(ItemBuilder.builder(Material.BLACK_STAINED_GLASS_PANE).setName("").build(), event -> event.setCancelled(true));
            gui.setOnGlobalClick(event -> event.setCancelled(true));
            if (i != 0) {
                border.addItem(new GuiItem(ItemBuilder.builder(Material.ARROW).setName(ChatColor.GREEN + "Go Back").setLore(ChatColor.GRAY + "To page " + i).build(), event -> pagPane.setPage(pagPane.getPage() - 1)), 0, 2);
            }
            if (i != numPages - 1) {
                border.addItem(new GuiItem(ItemBuilder.builder(Material.ARROW).setName(ChatColor.GREEN + "Go Forward").setLore(ChatColor.GRAY + "To page " + (i + 2)).build(), event -> pagPane.setPage(pagPane.getPage() - 1)), 8, 2);
            }
            border.addItem(new GuiItem(ItemBuilder.builder(Material.BARRIER).setName(ChatColor.RED + "Cancel").build(), event -> {
                player.closeInventory();
                removeCurrentlyChanging(player);
                GuiManager.setInventory(player, "spells");
            }), 0, 4);
            StaticPane spellList = new StaticPane(1, 1, 7, 3);
            spellList.fillWith(ItemBuilder.builder(Material.GRAY_STAINED_GLASS_PANE).setName("").build());
            for (int j = count; j < count + 27; j++) {
                if (j > spells.size() - 1) {
                    break;
                }
                Spell spell = SpellManager.getRegisteredSpell(spells.get(j));
                List<String> lore = new ArrayList<>(TextUtils.wrap(spell.description(), 30, org.bukkit.ChatColor.GRAY));
                lore.add(ChatColor.DARK_GRAY + "Mana cost: " + spell.manaCost() + "" + StatType.MANA.symbol());
                spellList.addItem(new GuiItem(ItemBuilder.builder(Material.HEART_OF_THE_SEA).setName(spell.displayName()).setLore(lore.toArray(new String[0])).build(),
                        event -> {
                            Integer changing = getCurrentlyChanging(player);
                            if (changing == null) {
                                return;
                            }
                            SpellPreferences.setSpellInSlot(player, changing, spell.id());
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 0.9f);
                            player.sendMessage(ChatColor.GREEN + "Successfully changed slot " + (changing + 1) + " to the spell " + spell.displayName());
                            player.closeInventory();
                            removeCurrentlyChanging(player);
                            GuiManager.setInventory(player, "spells");
                        }
                ), j - count % 9, (j - count) / 9);
            }
            pagPane.addPane(i, border);
            pagPane.addPane(i, spellList);
            count += 27;
        }
        gui.addPane(pagPane);
        pagPane.setPage(0);
        return gui;
    }
}
