package de.featjar.comparison.test;

import de.ovgu.featureide.fm.core.base.IFeatureModel;

import java.util.List;
import java.util.Set;

public interface ITestLibrary {
    Result<Boolean> isTautology(IFeatureModel featureModel, String[] parameters);
    Result<Boolean> isVoid(IFeatureModel featureModel);
    Result<Set<String>> coreFeatures(IFeatureModel featureModel);
    Result<Set<String>> deadFeatures(IFeatureModel featureModel);
    Result<Set<String>> falseOptional(IFeatureModel featureModel);
    Result<Set<String>> redundantConstraints(IFeatureModel featureModel);
    Result<List<Set<String>>> atomicSets(IFeatureModel featureModel);
    Result<Set<String>> indeterminedHiddenFeatures(IFeatureModel featureModel);
}
