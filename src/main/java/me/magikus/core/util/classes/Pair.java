package me.magikus.core.util.classes;

public record Pair<A, B>(A a, B b) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        return ((Pair<?, ?>) obj).a.equals(a) && ((Pair<?, ?>) obj).b.equals(b);
    }
}
