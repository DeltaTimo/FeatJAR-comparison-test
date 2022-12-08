package de.featjar.comparison.test.helper;

import java.util.List;
import java.util.Set;

public interface IModification<T> {
    Object addFeature(T featureModel, String fileName);
    Object removeFeature(T featureModel);
    Object addConstraint(T featureModel, String fileName);
    Object removeConstraint(T featureModel);
    Object slice(T featureModel,  String fileName);
    Object comparatorSpecialization(T featureModel, String fileName);
    Object comparatorGeneralization(T featureModel,  String fileName);
}
