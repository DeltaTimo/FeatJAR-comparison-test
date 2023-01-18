package de.featjar.comparison.test.helper;

public interface IBase<T, S> {
    T load(String filepath);
    T loadFromSource(String content, String filepath);
    S createQueryImpl(String a, String b);
    S createQueryAndNot(String a, String b);
    String loadConfiguration(String filepath);
    Object getFormula(Object featureModel);
    Object smoothFormula(T formula);
}
