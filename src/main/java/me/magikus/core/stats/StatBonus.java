package me.magikus.core.stats;

public class StatBonus {

    private final StatType t;
    private double bonus;

    public StatBonus(StatType t, double bonus) {
        this.t = t;
        this.bonus = bonus;
    }

    public StatType t() {
        return t;
    }

    public double bonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
