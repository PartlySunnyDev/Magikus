package me.magikus.core.magic.spells;

import me.magikus.core.tools.classes.Requirements;
import org.bukkit.entity.Player;

public abstract class Spell {
    private final String id;
    private final String description;
    private final Requirements reqs;
    private final String displayName;
    private final int manaCost;

    public Spell(String id, String description, String displayName, Requirements reqs, int manaCost) {
        this.id = id;
        this.description = description;
        this.reqs = reqs;
        this.displayName = displayName;
        this.manaCost = manaCost;
        SpellManager.registerSpell(this);
    }

    public String description() {
        return description;
    }

    public String id() {
        return id;
    }

    public Requirements reqs() {
        return reqs;
    }

    public String displayName() {
        return displayName;
    }

    public int manaCost() {
        return manaCost;
    }

    public abstract void castSpell(Player p);
}
