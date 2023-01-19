package de.featjar.comparison.test.helper;

/**
 *
 * Interface class with methods for analyses.
 *
 * @author Katjana Herbst
 * @since 01-19-2023
 */
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
    Object redundantConstraints(T featureModel, String config);
    Object atomicSets(T featureModel);
    Object atomicSets(T featureModel, String config);
    Object indeterminedHiddenFeatures(T featureModel);
    Object indeterminedHiddenFeatures(T featureModel, String config);
    Object countSolutions(T featureModel);
    Object countSolutions(T featureModel, String config);
    Object parseConfig(String config, Object variables);
}
