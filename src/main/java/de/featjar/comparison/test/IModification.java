package de.featjar.comparison.test;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

import java.util.List;
import java.util.Set;

public interface IModification {
    Result<Set<String>> addFeature(IFeatureModel featureModel);
    Result<List<String>> removeFeature(IFeatureModel featureModel);
    Result<Set<String>> addConstraint(IFeatureModel featureModel);
    Result<Set<String>> removeConstraint(IFeatureModel featureModel);
    Result<Set<String>> slice(IFeatureModel featureModel);
    Result<String> comparatorSpecialization(IFeatureModel featureModel);
    Result<String> comparatorGeneralization(IFeatureModel featureModel);
}
