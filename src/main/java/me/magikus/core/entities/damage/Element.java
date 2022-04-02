package me.magikus.core.entities.damage;

public enum Element {

    FIRE("water", "earth", "fire"),
    ;
    private final String weakness;
    private final String strength;
    private final String id;

    Element(String weakness, String strength, String id) {
        this.weakness = weakness;
        this.strength = strength;
        this.id = id;
    }

    public String weakness() {
        return weakness;
    }

    public String strength() {
        return strength;
    }

    public Element elementWeakness() {
        return getElement(weakness);
    }

    public Element elementStrength() {
        return getElement(strength);
    }

    public String id() {
        return id;
    }

    public static Element getElement(String id) {
        for (Element e : values()) {
            if (e.id().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
