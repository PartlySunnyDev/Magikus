package me.magikus.additions;

import me.magikus.additions.ascensions.EnlightenedAscension;
import me.magikus.additions.enchant.SharpnessEnchant;
import me.magikus.additions.stat.Infusion;
import me.magikus.additions.stat.ZombieProof;

public class AdditionRegister {

    public static void registerAdditions() {
        new Infusion();
        new ZombieProof();
    }

    public static void registerAscensions() {
        new EnlightenedAscension();
    }

    public static void registerEnchants() {
        new SharpnessEnchant();
    }

}
