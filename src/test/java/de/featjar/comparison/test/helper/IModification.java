package de.featjar.comparison.test.helper;

/**
 *
 * Interface class with methods for modifications on featuremodels.
 *
 * @author Katjana Herbst
 * @since 01-19-2023
 */
public interface IModification<T> {
    Object addFeature(T featureModel, String fileName);
    Object removeFeature(T featureModel);
    Object addConstraint(T featureModel, String fileName);
    Object removeConstraint(T featureModel);
    Object slice(T featureModel,  String fileName);
    Object comparatorSpecialization(T featureModel, String fileName);
    Object comparatorGeneralization(T featureModel,  String fileName);
}
