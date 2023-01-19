package de.featjar.comparison.test.helper;

/**
 *
 * Interface class with methods for basic operations for featuremodels.
 *
 * @author Katjana Herbst
 * @since 01-19-2023
 */
public interface IBase<T, S> {
    T load(String filepath);
    T loadFromSource(String content, String filepath);
    S createQueryImpl(String a, String b);
    S createQueryAndNot(String a, String b);
    String loadConfiguration(String filepath);
    Object getFormula(Object featureModel);
    Object smoothFormula(T formula);
}
