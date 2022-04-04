package me.magikus.core.magic.spells;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SpellManager {

    private static final Map<String, Spell> spells = new HashMap<>();

    public static void registerSpell(Spell spell) {
        spells.put(spell.id(), spell);
    }

    public static Spell getSpell(String id) {
        return spells.get(id);
    }

    public static void castSpell(String id, Player p) {
        if (getSpell(id) != null) {
            getSpell(id).castSpell(p);
        }
    }

}
