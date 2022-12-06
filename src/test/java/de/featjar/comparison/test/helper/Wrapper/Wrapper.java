package de.featjar.comparison.test.helper.Wrapper;

public class Wrapper {
    private Object library1;
    private Object library2;

    public Wrapper(Object library1, Object library2) {
        this.library1 = library1;
        this.library2 = library2;
    }

    public Object getObjectLib1() {
        return library1;
    }
    public Object getObjectLib2() {
        return library2;
    }
}
