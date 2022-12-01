package de.featjar.comparison.test.helper;

public interface IBase<T> {
    T load(String filepath);
    T loadFromSource(String content, String filepath);
}
