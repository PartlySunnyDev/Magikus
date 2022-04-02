package me.magikus.core.items.additions;

import me.magikus.core.items.MagikusItem;

public abstract class Addition {

    protected final AdditionInfo type;
    protected MagikusItem parent;
    protected int amount = 1;

    public Addition(AdditionInfo type, MagikusItem parent) {
        this.type = type;
        this.parent = parent;
    }

    public MagikusItem parent() {
        return parent;
    }

    public AdditionInfo type() {
        return type;
    }

    public int amount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
