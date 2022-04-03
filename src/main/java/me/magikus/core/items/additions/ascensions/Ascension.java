package me.magikus.core.items.additions.ascensions;

import me.magikus.Magikus;
import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.stats.StatList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Ascension implements Listener {

    private static final ArrayList<String> registered = new ArrayList<>();
    private final String id;
    private final String displayName;
    private final String bonusDesc;
    private final HashMap<Rarity, StatList> statBonuses;
    private final ItemType[] canApply;
    private MagikusItem parent;

    public Ascension(String id, String displayName, String bonusDesc, HashMap<Rarity, StatList> statBonuses, ItemType... canApply) {
        this(id, displayName, bonusDesc, statBonuses, null, canApply);
    }

    public Ascension(String id, String displayName, String bonusDesc, HashMap<Rarity, StatList> statBonuses, MagikusItem parent, ItemType... canApply) {
        this.parent = parent;
        this.id = id;
        this.displayName = displayName;
        this.bonusDesc = bonusDesc;
        this.statBonuses = statBonuses;
        this.canApply = canApply;
        AscensionManager.addAscension(this);
        if (!registered.contains(id)) {
            Magikus plugin = JavaPlugin.getPlugin(Magikus.class);
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            registered.add(id);
        }
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

    public String bonusDesc() {
        return bonusDesc;
    }

    public HashMap<Rarity, StatList> statBonuses() {
        return statBonuses;
    }

    public MagikusItem parent() {
        return parent;
    }

    public void setParent(MagikusItem parent) {
        this.parent = parent;
    }
}
