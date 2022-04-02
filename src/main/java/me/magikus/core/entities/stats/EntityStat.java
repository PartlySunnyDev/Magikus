package me.magikus.core.entities.stats;

public class EntityStat {
    private final EntityStatType type;
    private double value;

    public EntityStat(EntityStatType type, double value) {
        this.type = type;
        this.value = value;
    }

    public EntityStatType type() {
        return type;
    }

    public double value() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
