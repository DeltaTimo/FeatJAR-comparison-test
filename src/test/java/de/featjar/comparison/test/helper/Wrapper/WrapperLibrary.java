package de.featjar.comparison.test.helper.Wrapper;

public class WrapperLibrary {
    private LibraryObject library1;
    private LibraryObject library2;

    public WrapperLibrary(LibraryObject library1, LibraryObject library2) {
        this.library1 = library1;
        this.library2 = library2;
    }

    public LibraryObject getObjectLib1() {
        return this.library1;
    }
    public LibraryObject getObjectLib2() {
        return this.library2;
    }
}
