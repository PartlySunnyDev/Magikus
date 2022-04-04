package me.magikus.core.items.abilities;

public enum AbilityType {
    PASSIVE(false),
    LEFT_CLICK(true),
    RIGHT_CLICK(true),
    PIECE_BONUS(false),
    PASSIVE_BONUS(false),
    FULL_SET_BONUS(false);

    private final boolean weapon;

    AbilityType(boolean weapon) {
        this.weapon = weapon;
    }

    public boolean weapon() {
        return weapon;
    }
}
