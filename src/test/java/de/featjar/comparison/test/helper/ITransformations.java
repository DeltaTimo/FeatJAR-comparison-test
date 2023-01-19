package de.featjar.comparison.test.helper;

/**
 *
 * Interface class with methods for transformations of featuremodel formats.
 *
 * @author Katjana Herbst
 * @since 01-19-2023
 */
public interface ITransformations<T> {
    Object getDimacs(T featureModel);
    Object getCNF(T featureModel);
    Object getUVL(T featureModel);
    Object getVelvet(T featureModel);
    Object getSxfml(T featureModel);
}
