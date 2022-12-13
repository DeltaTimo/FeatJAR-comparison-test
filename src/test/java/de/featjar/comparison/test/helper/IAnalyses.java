package de.featjar.comparison.test.helper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAnalyses<T, S> {
    Object isTautology(T featureModel, S query);
    Object isVoid(T featureModel);
    Object isVoid(T featureModel, String config);
    Object coreFeatures(T featureModel);
    Object coreFeatures(T featureModel, String config);
    Object deadFeatures(T featureModel);
    Object deadFeatures(T featureModel, String config);
    Object falseOptional(T featureModel);
    Object falseOptional(T featureModel, String config);
    Object redundantConstraints(T featureModel);
    Object atomicSets(T featureModel);
    Object indeterminedHiddenFeatures(T featureModel);
    Object countSolutions(T featureModel);
    Object parseConfig(String config, Object variables);
}
