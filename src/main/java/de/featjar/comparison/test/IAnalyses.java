package de.featjar.comparison.test;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.prop4j.Node;

import java.util.List;
import java.util.Set;

public interface IAnalyses {
    Result<Boolean> isTautology(IFeatureModel featureModel, Node query);
    Result<Boolean> isVoid(IFeatureModel featureModel);
    Result<Set<String>> coreFeatures(IFeatureModel featureModel);
    Result<Set<String>> deadFeatures(IFeatureModel featureModel);
    Result<Set<String>> falseOptional(IFeatureModel featureModel);
    Result<Set<String>> redundantConstraints(IFeatureModel featureModel);
    Result<List<Set<String>>> atomicSets(IFeatureModel featureModel);
    Result<Set<String>> indeterminedHiddenFeatures(IFeatureModel featureModel);
    Result<Long> countSolutions(IFeatureModel featureModel);
}
