package me.magikus.core.items.additions.enchants;

import me.magikus.Magikus;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.stats.StatList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Enchant implements Listener {

    private static final ArrayList<String> registered = new ArrayList<>();
    private final String id;
    private final int maxLevel;
    private final Class<? extends Enchant> cl;
    private final String displayName;
    private final ItemType[] canApply;
    private final boolean isUltra;
    private final ChatColor enchantColor;
    private int level;
    private MagikusItem parent;

    public Enchant(String id, String displayName, int maxLevel, int level, Class<? extends Enchant> cl, boolean isUltra, ItemType... canApply) {
        this(id, displayName, maxLevel, level, cl, null, isUltra, canApply);
    }

    public Enchant(String id, String displayName, int maxLevel, int level, Class<? extends Enchant> cl, MagikusItem parent, boolean isUltra, ItemType... canApply) {
        this(id, displayName, maxLevel, level, cl, parent, isUltra, ChatColor.GRAY, canApply);
    }

    public Enchant(String id, String displayName, int maxLevel, int level, Class<? extends Enchant> cl, MagikusItem parent, boolean isUltra, ChatColor enchantColor, ItemType... canApply) {
        this.parent = parent;
        this.id = id;
        this.displayName = displayName;
        this.canApply = canApply;
        this.maxLevel = maxLevel;
        this.level = level;
        this.cl = cl;
        this.isUltra = isUltra;
        this.enchantColor = enchantColor;
        EnchantManager.addEnchant(this);
        if (!registered.contains(id)) {
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            registered.add(id);
        }
    }

    public boolean isUltra() {
        return isUltra;
    }

    public boolean isConflicting(Enchant other) {
        return false;
    }

    public Class<? extends Enchant> cl() {
        return cl;
    }

    public boolean cannotApply(MagikusItem item) {
        return !List.of(canApply).contains(item.type());
    }

    public int maxLevel() {
        return maxLevel;
    }

    public int level() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ItemType[] canApply() {
        return canApply;
    }

    public boolean canApply(MagikusItem i) {
        return List.of(canApply()).contains(i.type());
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public abstract String description();

    public abstract StatList getBonusStats(@Nullable Player player, @Nullable Entity target);

    public MagikusItem parent() {
        return parent;
    }

    public void setParent(MagikusItem parent) {
        this.parent = parent;
    }

    public ChatColor enchantColor() {
        return enchantColor;
    }
}
