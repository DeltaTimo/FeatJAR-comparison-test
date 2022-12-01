package de.featjar.comparison.test.helper;

import java.util.List;
import java.util.Set;

public interface IModification<T> {
    Result<Set<String>> addFeature(T featureModel);
    Result<List<String>> removeFeature(T featureModel);
    Result<Set<String>> addConstraint(T featureModel);
    Result<Set<String>> removeConstraint(T featureModel);
    Result<Set<String>> slice(T featureModel);
    Result<String> comparatorSpecialization(T featureModel);
    Result<String> comparatorGeneralization(T featureModel);
}
