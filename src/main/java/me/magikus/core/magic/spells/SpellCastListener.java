package me.magikus.core.magic.spells;

import me.magikus.Magikus;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.player.PlayerUpdater;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.magikus.core.tools.util.NumberUtils.getIntegerStringOf;

public class SpellCastListener implements Listener {

    private static final Map<UUID, String> playerCurrentCombo = new HashMap<>();
    private static final Map<UUID, BukkitTask> playerExpireTasks = new HashMap<>();
    private static final int comboExpireTimeInTicks = 60;

    public static void initPlayers(Server s) {
        for (Player p : s.getOnlinePlayers()) {
            playerCurrentCombo.put(p.getUniqueId(), "");
        }
    }

    @EventHandler
    public void onPlayerStartSpellCast(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.isSneaking() && e.getHand() == EquipmentSlot.HAND) {
            ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
            if (!itemInMainHand.hasItemMeta()) {
                return;
            }
            MagikusItem item = MagikusItem.getItemFrom(itemInMainHand, e.getPlayer());
            if (item == null || !item.canCastSpells()) {
                return;
            }
            boolean isRightClick = (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK);
            String playerCombo = playerCurrentCombo.get(p.getUniqueId());
            playerCombo = playerCombo + (isRightClick ? "r" : "l");
            playerCurrentCombo.put(p.getUniqueId(), playerCombo);
            p.sendTitle(getFormattedSpellText(playerCombo), "", (int) Math.floor(comboExpireTimeInTicks / 6f), comboExpireTimeInTicks - 2 * (int) Math.floor(comboExpireTimeInTicks / 6f), (int) Math.floor(comboExpireTimeInTicks / 6f));
            if (playerCombo.length() == 3) {
                if (playerExpireTasks.containsKey(p.getUniqueId())) {
                    playerExpireTasks.get(p.getUniqueId()).cancel();
                }
                playerCurrentCombo.put(p.getUniqueId(), "");
                int castSpellSlot = -1;
                for (int i = 0; i < SpellPreferences.spellSlotsUnlocked(p); i++) {
                    if (SpellPreferences.getComboForSlot(p, i).equalsIgnoreCase(playerCombo)) {
                        SpellManager.castSpell(SpellPreferences.getSpellInSlot(p, i), p);
                        castSpellSlot = i;
                    }
                }
                if (castSpellSlot == -1) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                    PlayerUpdater.sendMessageToPlayer(p, ChatColor.RED + "Spell not discovered / existent!", 40);
                    return;
                }
                Spell spell = SpellManager.getRegisteredSpell(SpellPreferences.getSpellInSlot(p, castSpellSlot));
                if (spell == null) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                    PlayerUpdater.sendMessageToPlayer(p, ChatColor.RED + "Spell not discovered / existent!", 40);
                    return;
                }
                if (!spell.reqs().doesPlayerSatisfy(p)) {
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                    PlayerUpdater.sendMessageToPlayer(p, ChatColor.RED + "You don't satisfy this spell's requirements!", 40);
                    return;
                }
                double mana = PlayerStatManager.getStatWithBonus(p.getUniqueId(), StatType.MANA);
                if (mana < spell.manaCost()) {
                    StatList stats = PlayerStatManager.playerStats.get(p.getUniqueId());
                    PlayerUpdater.sendMessageToPlayer(p, ChatColor.RED + "" + getIntegerStringOf(stats.getStatWithBonus(StatType.HEALTH), 0) + "/" + getIntegerStringOf(stats.getStatWithBonus(StatType.MAX_HEALTH), 0) + "❤   " + ChatColor.RED + "" + ChatColor.BOLD + "OUT OF MANA", 40);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                    return;
                }
                PlayerStatManager.setStat(p.getUniqueId(), StatType.MANA, mana - spell.manaCost());
                PlayerUpdater.sendMessageToPlayer(p, ChatColor.AQUA + "Cast spell " + spell.displayName() + "! (-" + spell.manaCost() + " mana)", 40);
                p.sendTitle("", "", 0, 0, 0);
                return;
            }
            BukkitTask t = Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Magikus.class), () -> {
                if (playerCurrentCombo.containsKey(p.getUniqueId())) {
                    playerCurrentCombo.put(p.getUniqueId(), "");
                }
            }, comboExpireTimeInTicks);
            if (playerExpireTasks.containsKey(p.getUniqueId())) {
                playerExpireTasks.get(p.getUniqueId()).cancel();
            }
            playerExpireTasks.put(p.getUniqueId(), t);
        }
    }

    private String getFormattedSpellText(String combo) {
        StringBuilder sb = new StringBuilder();
        for (char c : combo.toCharArray()) {
            sb.append(ChatColor.LIGHT_PURPLE).append(Character.toUpperCase(c)).append(ChatColor.GRAY).append(" - ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        playerCurrentCombo.put(e.getPlayer().getUniqueId(), "");
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        playerCurrentCombo.remove(e.getPlayer().getUniqueId());
    }

}
