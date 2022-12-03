package de.featjar.comparison.test.helper;

import java.util.List;
import java.util.Set;

public interface IModification<T> {
    Result<Set<String>> addFeature(T featureModel, String fileName);
    Result<List<String>> removeFeature(T featureModel);
    Result<Set<String>> addConstraint(T featureModel, String fileName);
    Result<Set<String>> removeConstraint(T featureModel);
    Result<Set<String>> slice(T featureModel,  String fileName);
    Result<String> comparatorSpecialization(T featureModel, String fileName);
    Result<String> comparatorGeneralization(T featureModel,  String fileName);
}
