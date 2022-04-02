package me.magikus.core.items.abilities;

import me.magikus.Magikus;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.player.PlayerUpdater;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.AbilityUtils;
import me.magikus.core.util.DataUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.magikus.core.util.AbilityUtils.hasAbility;

public abstract class Ability implements Listener {

    private static final ArrayList<String> registered = new ArrayList<>();
    private static final Map<String, Map<UUID, Integer>> cooldowns = new HashMap<>();
    protected final String name;
    protected final String description;
    protected final int manaCost;
    protected final int cooldown;
    protected final AbilityType type;
    protected final String id;
    private final ItemType[] appliableTypes;
    private final MagikusItem parent;
    private final int cooldownRemaining = -1;
    protected boolean onCooldown = false;
    private String cooldownMessage = ChatColor.RED + "This ability is on cooldown!";

    public Ability(String id, String name, String description, AbilityType type, @Nullable MagikusItem parent, ItemType... appliableTypes) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.manaCost = 0;
        this.cooldown = 0;
        this.type = type;
        this.appliableTypes = appliableTypes;
        this.parent = parent;
        if (!registered.contains(id)) {
            cooldowns.put(id, new HashMap<>());
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            registered.add(id);
        }
    }

    public Ability(String id, String name, String description, int manaCost, int cooldown, AbilityType type, @Nullable MagikusItem parent, ItemType... appliableTypes) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.type = type;
        this.appliableTypes = appliableTypes;
        this.parent = parent;
        if (!registered.contains(id)) {
            cooldowns.put(id, new HashMap<>());
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            registered.add(id);
        }
    }

    public Ability(String id, String name, String description, int manaCost, int cooldown, AbilityType type, @Nullable MagikusItem parent, String cooldownMessage, ItemType... appliableTypes) {
        this(id, name, description, manaCost, cooldown, type, parent, appliableTypes);
        this.cooldownMessage = cooldownMessage;
    }

    public int cooldown() {
        return cooldown;
    }

    public String id() {
        return id;
    }

    public String cooldownMessage() {
        return cooldownMessage;
    }

    public int cooldownRemaining() {
        return cooldownRemaining;
    }

    public int manaCost() {
        return manaCost;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public AbilityType type() {
        return type;
    }

    public ItemType[] appliableTypes() {
        return appliableTypes;
    }

    public boolean fullSet(ItemStack stack, Player player) {
        MagikusItem i = DataUtils.getSkyblockItem(stack, player);
        if (i != null && AbilityUtils.hasAbility(stack, id)) {
            return i.hasFullSet();
        }
        return false;
    }

    public boolean pieceBonus(ItemStack stack, Player player) {
        MagikusItem i = DataUtils.getSkyblockItem(stack, player);
        if (i != null && AbilityUtils.hasAbility(stack, id)) {
            return i.pieceBonusActive();
        }
        return false;
    }

    protected boolean active(Player player) {
        ItemStack pieceWithAbility = playerHasArmorWithAbility(player.getInventory());
        if (type == AbilityType.FULL_SET_BONUS) {
            return fullSet(pieceWithAbility, player);
        } else if (type == AbilityType.PIECE_BONUS) {
            return pieceBonus(pieceWithAbility, player);
        } else {
            return false;
        }
    }

    public ItemStack playerHasArmorWithAbility(PlayerInventory inventory) {
        ItemStack pieceWithAbility = null;
        if (hasAbility(inventory.getBoots(), id)) {
            pieceWithAbility = inventory.getBoots();
        } else if (hasAbility(inventory.getHelmet(), id)) {
            pieceWithAbility = inventory.getHelmet();
        } else if (hasAbility(inventory.getChestplate(), id)) {
            pieceWithAbility = inventory.getChestplate();
        } else if (hasAbility(inventory.getLeggings(), id)) {
            pieceWithAbility = inventory.getLeggings();
        }
        return pieceWithAbility;
    }

    @Override
    public String toString() {
        switch (type) {
            case PASSIVE -> {
                return "";
            }
            case LEFT_CLICK -> {
                return "LEFT CLICK";
            }
            case RIGHT_CLICK -> {
                return "RIGHT CLICK";
            }
            case SHIFT_LEFT_CLICK -> {
                return "SNEAK LEFT CLICK";
            }
            case SHIFT_RIGHT_CLICK -> {
                return "SNEAK RIGHT CLICK";
            }
        }
        return "";
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (AbilityUtils.hasAbility(e.getPlayer().getInventory().getItemInMainHand(), this.id)) {
            if (onCooldown(e.getPlayer())) {
                e.getPlayer().sendMessage(cooldownMessage);
                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                return;
            }
            if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
                if (e.getPlayer().isSneaking() && type == AbilityType.SHIFT_LEFT_CLICK) {
                    if (mana(e.getPlayer())) {
                        trigger(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());
                        startCooldown(e.getPlayer());
                    }
                } else if (type == AbilityType.LEFT_CLICK) {
                    if (mana(e.getPlayer())) {
                        trigger(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());
                        startCooldown(e.getPlayer());
                    }
                }
            } else if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
                if (e.getPlayer().isSneaking() && type == AbilityType.SHIFT_RIGHT_CLICK) {
                    if (mana(e.getPlayer())) {
                        trigger(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());
                        startCooldown(e.getPlayer());
                    }
                } else if (type == AbilityType.RIGHT_CLICK) {
                    if (mana(e.getPlayer())) {
                        trigger(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand());
                        startCooldown(e.getPlayer());
                    }
                }
            }
        }
    }

    private boolean mana(Player p) {
        double mana = PlayerStatManager.getStat(p.getUniqueId(), StatType.MANA);
        if (mana < manaCost) {
            PlayerUpdater.sendPlayerDisplay(p, true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            return false;
        }
        PlayerStatManager.setStat(p.getUniqueId(), StatType.MANA, mana - manaCost);
        return true;
    }

    protected boolean onCooldown(Player p) {
        return cooldowns.get(id).get(p.getUniqueId()) != null && cooldowns.get(id).get(p.getUniqueId()) < 0;
    }

    protected void startCooldown(Player p) {
        cooldowns.get(id).put(p.getUniqueId(), cooldown);
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.get(id).put(p.getUniqueId(), cooldowns.get(id).get(p.getUniqueId()) - 1);
                if (!onCooldown(p)) {
                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Magikus.class), 0, 20);
    }

    protected abstract void trigger(Player player, ItemStack parent);
}
