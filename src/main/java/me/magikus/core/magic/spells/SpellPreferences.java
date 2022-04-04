package me.magikus.core.magic.spells;

import me.magikus.Magikus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SpellPreferences {

    private static final YamlConfiguration playerSpellPreferences = Magikus.configManager.getConfig("spellPreferences");

    public static int spellSlotsUnlocked(Player p) {
        createPlayerInstance(p.getUniqueId());
        ConfigurationSection sc = playerSpellPreferences.getConfigurationSection(p.getUniqueId().toString());
        return sc.getStringList("spellSlots").size();
    }

    public static String getComboForSlot(Player p, int slot) {
        createPlayerInstance(p.getUniqueId());
        ConfigurationSection sc = playerSpellPreferences.getConfigurationSection(p.getUniqueId().toString());
        List<String> s = sc.getStringList("spellCombos");
        if (slot >= s.size()) {
            return "";
        }
        return s.get(slot);
    }

    public static String getSpellInSlot(Player p, int slot) {
        createPlayerInstance(p.getUniqueId());
        ConfigurationSection sc = playerSpellPreferences.getConfigurationSection(p.getUniqueId().toString());
        List<String> s = sc.getStringList("spellSlots");
        if (slot >= s.size()) {
            return "";
        }
        return s.get(slot);

    }

    public static List<String> getDiscoveredSpells(Player p) {
        createPlayerInstance(p.getUniqueId());
        ConfigurationSection sc = playerSpellPreferences.getConfigurationSection(p.getUniqueId().toString());
        return sc.getStringList("unlockedSpells");
    }

    public static void setComboForSlot(Player p, int slot, String newCombo) {
        createPlayerInstance(p.getUniqueId());
        Magikus.configManager.saveConfig("spellPreferences", playerSpellPreferences);
    }

    public static void setSpellInSlot(Player p, int slot, String newSpell) {
        createPlayerInstance(p.getUniqueId());
        Magikus.configManager.saveConfig("spellPreferences", playerSpellPreferences);
    }

    public static void discoverSpell(Player p, String spell) {
        createPlayerInstance(p.getUniqueId());
        Magikus.configManager.saveConfig("spellPreferences", playerSpellPreferences);
    }

    private static void createPlayerInstance(UUID player) {
        if (!playerSpellPreferences.contains(player.toString())) {
            ConfigurationSection playerInfo = playerSpellPreferences.createSection(player.toString());
            playerInfo.set("spellCombos", List.of(new String[] {"RRR", "RLR", "RRL"}));
            playerInfo.set("unlockedSpells", List.of(new String[] {}));
            playerInfo.set("spellSlots", List.of(new String[] {"", "", ""}));
            Magikus.configManager.saveConfig("spellPreferences", playerSpellPreferences);
        }
    }

}
