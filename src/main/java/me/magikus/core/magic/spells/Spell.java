package me.magikus.core.magic.spells;

import me.magikus.core.util.Requirements;
import org.bukkit.entity.Player;

public abstract class Spell {
    private final String id;
    private final Requirements reqs;
    private final String displayName;
    private final int manaCost;

    public Spell(String id, String displayName, Requirements reqs, int manaCost) {
        this.id = id;
        this.reqs = reqs;
        this.displayName = displayName;
        this.manaCost = manaCost;
        SpellManager.registerSpell(this);
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
