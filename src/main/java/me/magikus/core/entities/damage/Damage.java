package me.magikus.core.entities.damage;

public class Damage {

    private final double damage;
    private final Element element;
    private final DamageType type;

    public Damage(double amount, Element element, DamageType type) {
        this.damage = amount;
        this.element = element;
        this.type = type;
    }

    public double damage() {
        return damage;
    }

    public Element element() {
        return element;
    }

    public DamageType type() {
        return type;
    }
}
