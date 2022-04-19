package me.magikus.abilities;

import me.magikus.abilities.rightClick.SmiteAbility;
import me.magikus.abilities.rightClick.WitherImpactAbility;

public class AbilityRegister {

    public static void registerAbilities() {
        new SmiteAbility();
        new WitherImpactAbility();
    }

}
