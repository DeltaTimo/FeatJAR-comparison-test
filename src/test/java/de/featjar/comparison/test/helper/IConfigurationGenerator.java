package de.featjar.comparison.test.helper;

public interface IConfigurationGenerator<T> {
    Object pairwise(T featureModel);
    Object icpl(T featureModel);
    Object chvatal(T featureModel);
    Object all(T featureModel);
    Object random(T featureModel);
}
