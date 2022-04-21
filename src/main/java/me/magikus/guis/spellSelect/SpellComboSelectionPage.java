package me.magikus.guis.spellSelect;

import com.github.stefvanschie.inventoryframework.adventuresupport.StringHolder;
import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import me.magikus.core.gui.GuiInstance;
import me.magikus.core.gui.GuiManager;
import me.magikus.core.magic.spells.SpellPreferences;
import me.magikus.core.tools.classes.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpellComboSelectionPage implements GuiInstance {

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
        if (!(e instanceof Player player)) return new ChestGui(3, "");
        List<String> combos = SpellPreferences.getCombos(player);
        ChestGui gui = new ChestGui(3, StringHolder.of(ChatColor.BLUE + "Change Combo"));
        Integer slotToChange = getCurrentlyChanging(player);
        Integer slot = slotToChange;
        if (slot == null) {
            return gui;
        }
        StaticPane pane = new StaticPane(0, 0, 9, 7);
        Label l = new Label(1, 1, 1, 1, Font.WHITE);
        Label l2 = new Label(2, 1, 1, 1, Font.WHITE);
        Label l3 = new Label(3, 1, 1, 1, Font.WHITE);
        l.setText("R", (character, item) -> {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Character.toString(character));
            item.setItemMeta(meta);
            return new GuiItem(item);
        });
        l2.setText("R", (character, item) -> {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Character.toString(character));
            item.setItemMeta(meta);
            return new GuiItem(item);
        });
        l3.setText("R", (character, item) -> {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Character.toString(character));
            item.setItemMeta(meta);
            return new GuiItem(item);
        });
        setClickAction(l, player, gui);
        setClickAction(l2, player, gui);
        setClickAction(l3, player, gui);
        gui.addPane(l);
        gui.addPane(l2);
        gui.addPane(l3);
        pane.addItem(new GuiItem(ItemBuilder.builder(Material.GREEN_CONCRETE).setName(ChatColor.GREEN + "Confirm").build(), event -> {
            String selection = l.getText() + l2.getText() + l3.getText();
            if (combos.contains(selection)) {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(ChatColor.RED + "One of your slots are already assigned to this value!!!");
                return;
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 0.9f);
            player.sendMessage(ChatColor.GREEN + "Successfully changed combo " + (slotToChange + 1) + " to " + selection);
            SpellPreferences.setComboForSlot(player, slotToChange, selection);
            removeCurrentlyChanging(player);
            player.closeInventory();
            GuiManager.setInventory(player, "spells");
        }), 6, 1);
        pane.addItem(new GuiItem(ItemBuilder.builder(Material.RED_CONCRETE).setName(ChatColor.RED + "Cancel").build(), event -> {
            player.closeInventory();
            removeCurrentlyChanging(player);
            GuiManager.setInventory(player, "spells");
        }), 7, 1);
        gui.addPane(pane);
        OutlinePane background = new OutlinePane(0, 0, 9, 7, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(ItemBuilder.builder(Material.GRAY_STAINED_GLASS_PANE).build()));
        background.setRepeat(true);
        gui.addPane(background);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        return gui;
    }

    private void setClickAction(Label l, Player player, Gui gui) {
        l.setOnClick(event -> {
            String newChar;
            if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                newChar = "R";
            } else if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                newChar = "L";
            } else {
                return;
            }
            l.setText(newChar, (character, item) -> {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Character.toString(character));
                item.setItemMeta(meta);
                return new GuiItem(item);
            });
            player.playSound(player.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, 1, 1);
            gui.update();
        });
    }
}
