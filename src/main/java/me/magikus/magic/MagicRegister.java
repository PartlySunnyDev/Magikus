package me.magikus.magic;

import me.magikus.magic.spells.HeavyHitSpell;
import me.magikus.magic.spells.PulseSpell;
import me.magikus.magic.spells.ZapSpell;

public class MagicRegister {

    public static void registerSpells() {
        new ZapSpell();
        new HeavyHitSpell();
        new PulseSpell();
    }

}
