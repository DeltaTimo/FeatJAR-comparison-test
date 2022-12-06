package de.featjar.comparison.test.helper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAnalyses<T, S> {
    Result<Boolean> isTautology(T featureModel, S query);
    Result<Boolean> isVoid(T featureModel);
    Result<Boolean> isVoid(T featureModel, String config);
    Result<Set<String>> coreFeatures(T featureModel);
    Result<Set<String>> deadFeatures(T featureModel);
    Result<Set<String>> falseOptional(T featureModel);
    Result<Set<String>> redundantConstraints(T featureModel);
    Result<List<Set<String>>> atomicSets(T featureModel);
    Result<Set<String>> indeterminedHiddenFeatures(T featureModel);
    Result<Long> countSolutions(T featureModel);
}
