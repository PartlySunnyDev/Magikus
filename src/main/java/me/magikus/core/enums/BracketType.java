package me.magikus.core.enums;

public enum BracketType {
    REGULAR("(", ")"),
    SQUARE("[", "]"),
    SQUIGGLY("{", "}");

    private final String start;
    private final String end;

    BracketType(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String start() {
        return start;
    }

    public String end() {
        return end;
    }
}
