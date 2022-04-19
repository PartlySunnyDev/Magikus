package me.magikus.core.magic.spells;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SpellManager {

    private static final Map<String, Spell> spells = new HashMap<>();

    public static void registerSpell(Spell spell) {
        spells.put(spell.id(), spell);
    }

    public static void unregisterSpell(String id) {
        spells.remove(id);
    }

    public static Spell getRegisteredSpell(String id) {
        return spells.get(id);
    }

    public static void castSpell(String id, Player p) {
        if (getRegisteredSpell(id) != null) {
            getRegisteredSpell(id).castSpell(p);
        }
    }

}
