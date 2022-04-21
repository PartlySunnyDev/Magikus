package me.magikus.core.player;

import me.magikus.Magikus;
import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.util.DataUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.magikus.core.tools.util.NumberUtils.getIntegerStringOf;

public class PlayerUpdater implements Listener {

    public static final Map<UUID, Pair<String, Integer>> messagesToSend = new HashMap<>();
    public static final Map<UUID, Integer> timeRemaining = new HashMap<>();

    public PlayerUpdater(Server s) {
        new ConstantUpdater(s).runTaskTimer(JavaPlugin.getPlugin(Magikus.class), 0, 1);
        new NaturalRegeneration(s).runTaskTimer(JavaPlugin.getPlugin(Magikus.class), 0, 20);
    }

    public static StatList getStats(Player player, boolean ignoreHand, @Nullable Entity targetEntity) {
        UUID id = player.getUniqueId();
        StatList oldStats = PlayerStatManager.playerStats.get(id);
        StatList newStats = BaseStatManager.getStatWithBonusListOf(id);
        PlayerInventory inventory = player.getInventory();
        MagikusItem helmet = DataUtils.getMagikusItem(inventory.getHelmet(), player);
        MagikusItem chestplate = DataUtils.getMagikusItem(inventory.getChestplate(), player);
        MagikusItem leggings = DataUtils.getMagikusItem(inventory.getLeggings(), player);
        MagikusItem boots = DataUtils.getMagikusItem(inventory.getBoots(), player);
        MagikusItem hand = DataUtils.getMagikusItem(inventory.getItemInMainHand(), player);
        if (ignoreHand) {
            hand = null;
        }
        if (helmet != null) {
            newStats = newStats.merge(helmet.getCombinedStats(player, targetEntity));
            newStats = newStats.merge(helmet.getEnchantStats(player, targetEntity));
        }
        if (chestplate != null) {
            newStats = newStats.merge(chestplate.getCombinedStats(player, targetEntity));
            newStats = newStats.merge(chestplate.getEnchantStats(player, targetEntity));
        }
        if (leggings != null) {
            newStats = newStats.merge(leggings.getCombinedStats(player, targetEntity));
            newStats = newStats.merge(leggings.getEnchantStats(player, targetEntity));
        }
        if (boots != null) {
            newStats = newStats.merge(boots.getCombinedStats(player, targetEntity));
            newStats = newStats.merge(boots.getEnchantStats(player, targetEntity));
        }
        if (hand != null) {
            newStats = newStats.merge(hand.getCombinedStats(player, targetEntity));
            newStats = newStats.merge(hand.getEnchantStats(player, targetEntity));
        }
        if (BaseStatManager.hasInitializedChangableStats.get(id)) {
            newStats.addStat(new Stat(StatType.HEALTH, oldStats.getStatWithBonus(StatType.HEALTH)));
            newStats.addStat(new Stat(StatType.MANA, oldStats.getStatWithBonus(StatType.MANA)));
        } else {
            newStats.addStat(new Stat(StatType.HEALTH, newStats.getStatWithBonus(StatType.MAX_HEALTH)));
            newStats.addStat(new Stat(StatType.MANA, newStats.getStatWithBonus(StatType.MAX_MANA)));
            DamageManager.updatePlayerHealthBar(player, newStats.getStatWithBonus(StatType.HEALTH), newStats.getStatWithBonus(StatType.MAX_HEALTH));
            PlayerStatManager.playerStats.put(player.getUniqueId(), newStats);
            BaseStatManager.hasInitializedChangableStats.put(id, true);
        }
        double speedCap = newStats.getStatWithBonus(StatType.SPEED_CAP);
        //ConsoleLogger.console(String.valueOf(speedCap));
        //ConsoleLogger.console(String.valueOf(newStats.getStatWithBonus(StatType.SPEED)));
        if (newStats.getStatWithBonus(StatType.SPEED) > speedCap) {
            newStats.addStat(new Stat(StatType.SPEED, speedCap));
        }
        player.setWalkSpeed((float) (newStats.getStatWithBonus(StatType.SPEED) / 500));
        player.setFlySpeed((float) (newStats.getStatWithBonus(StatType.SPEED) / 500));
        return newStats;
    }

    public static void updatePlayer(Player player) {
        StatList stats = getStats(player, false, null);
        double health = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.HEALTH);
        double speed = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.SPEED);
        double speedCap = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.SPEED_CAP);
        double maxHealth = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.MAX_HEALTH);
        if (speed > speedCap) {
            speed = speedCap;
        }
        //net.minecraft.world.entity.player.Player pplayer = ((CraftPlayer) player).getHandle();
        //AttributeInstance i = pplayer.getAttribute(Attributes.MOVEMENT_SPEED);
        //AttributeModifier mod = new AttributeModifier(movementSpeedUUID, "speed stat", speed / 100, AttributeModifier.Operation.MULTIPLY_BASE);
        //for (AttributeModifier m : i.getModifiers()) {
        //    i.removeModifier(m.getId());
        //}
        //i.addTransientModifier(mod);
        if (speed / 500 > 1) {
            speed = 1;
        }
        player.setWalkSpeed((float) (speed / 500));
        player.setFlySpeed((float) (speed / 500));
        DamageManager.updatePlayerHealthBar(player, health, maxHealth);
        PlayerStatManager.playerStats.put(player.getUniqueId(), stats);
    }

    public static void sendPlayerDisplay(Player p) {
        StatList stats = getStats(p, false, null);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getPlayerDisplay(stats)));
    }

    public static String getPlayerDisplay(StatList stats) {
        return ChatColor.RED + "" + getIntegerStringOf(stats.getStatWithBonus(StatType.HEALTH), 0) + "/" + getIntegerStringOf(stats.getStatWithBonus(StatType.MAX_HEALTH), 0) + "❤   " + ChatColor.AQUA + "" + getIntegerStringOf(stats.getStatWithBonus(StatType.MANA), 0) + "/" + getIntegerStringOf(stats.getStatWithBonus(StatType.MAX_MANA), 0) + StatType.MANA.symbol();
    }

    public static void sendMessageToPlayer(Player p, String message, int timeInTicks) {
        messagesToSend.put(p.getUniqueId(), new Pair<>(message, timeInTicks));
        timeRemaining.put(p.getUniqueId(), timeInTicks);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void hit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p) {
            updatePlayer(p);
        }
    }
}

class NaturalRegeneration extends BukkitRunnable {
    private final Server s;

    public NaturalRegeneration(Server s) {
        this.s = s;
    }

    @Override
    public void run() {
        for (Player p : s.getOnlinePlayers()) {
            UUID uniqueId = p.getUniqueId();
            double regenSpeed = PlayerStatManager.getStatWithBonus(uniqueId, StatType.HEALTH_REGEN_SPEED);
            double maxHealth = PlayerStatManager.getStatWithBonus(uniqueId, StatType.MAX_HEALTH);
            double health = PlayerStatManager.getStatWithBonus(uniqueId, StatType.HEALTH);
            double maxMana = PlayerStatManager.getStatWithBonus(uniqueId, StatType.MAX_MANA);
            double manaSpeed = PlayerStatManager.getStatWithBonus(uniqueId, StatType.MANA_REGEN_SPEED);
            double mana = PlayerStatManager.getStatWithBonus(uniqueId, StatType.MANA);
            PlayerStatManager.setStat(uniqueId, StatType.HEALTH, Math.min(health + (maxHealth * (regenSpeed / 100)), maxHealth));
            PlayerStatManager.setStat(uniqueId, StatType.MANA, Math.min(mana + (maxMana * (manaSpeed / 100)), maxMana));
        }
    }
}

class ConstantUpdater extends BukkitRunnable {
    private final Server s;

    public ConstantUpdater(Server s) {
        this.s = s;
    }

    @Override
    public void run() {
        for (Player p : s.getOnlinePlayers()) {
            Pair<String, Integer> specialMessage = PlayerUpdater.messagesToSend.get(p.getUniqueId());
            if (specialMessage != null) {
                if (PlayerUpdater.timeRemaining.get(p.getUniqueId()) < 1) {
                    PlayerUpdater.messagesToSend.put(p.getUniqueId(), null);
                    PlayerUpdater.timeRemaining.put(p.getUniqueId(), 0);
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(specialMessage.a()));
                    PlayerUpdater.timeRemaining.put(p.getUniqueId(), PlayerUpdater.timeRemaining.get(p.getUniqueId()) - 1);
                }
            } else {
                PlayerUpdater.sendPlayerDisplay(p);
            }
        }
        for (Player p : s.getOnlinePlayers()) {
            PlayerUpdater.updatePlayer(p);
        }
    }
}
