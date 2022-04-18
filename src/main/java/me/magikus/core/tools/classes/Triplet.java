package me.magikus.core.tools.classes;

public class Triplet<A, B, C> {

    private A a;
    private B b;
    private C c;

    public Triplet(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A a() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B b() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C c() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triplet)) {
            return false;
        }
        return ((Triplet<?, ?, ?>) obj).a.equals(a) && ((Triplet<?, ?, ?>) obj).b.equals(b) && ((Triplet<?, ?, ?>) obj).c.equals(c);
    }
}

