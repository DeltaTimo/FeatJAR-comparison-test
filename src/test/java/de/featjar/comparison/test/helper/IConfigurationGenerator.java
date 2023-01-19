package de.featjar.comparison.test.helper;

/**
 *
 * Interface class with methods for configuration generation.
 *
 * @author Katjana Herbst
 * @since 01-19-2023
 */
public interface IConfigurationGenerator<T> {
    Object pairwise(T featureModel);
    Object icpl(T featureModel);
    Object chvatal(T featureModel);
    Object all(T featureModel);
    Object random(T featureModel);
}
