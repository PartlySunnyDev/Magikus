package me.magikus.core.items.abilities;

import java.util.HashMap;
import java.util.Map;

public class AbilityList {

    private final Map<AbilityType, Ability> abilityList = new HashMap<>();

    public AbilityList() {
    }

    public AbilityList(Ability... abilities) {
        for (Ability a : abilities) {
            abilityList.put(a.type, a);
        }
    }

    public void addAbility(Ability ability) {
        abilityList.put(ability.type(), ability);
    }

    public void removeAbility(AbilityType type) {
        abilityList.remove(type);
    }

    public void removeAbility(Ability ability) {
        abilityList.remove(ability.type());
    }

    public Ability[] asList() {
        Ability[] returned = new Ability[abilityList.size()];
        int count = 0;
        for (Ability s : abilityList.values()) {
            returned[count] = s;
            count++;
        }
        return returned;
    }

    public AbilityList merge(AbilityList other) {
        abilityList.putAll(other.abilityList);
        return this;
    }

    public boolean contains(AbilityType type) {
        for (AbilityType t : abilityList.keySet()) {
            if (t == type) {
                return true;
            }
        }
        return false;
    }
}
