package de.featjar.comparison.test.helper;

public interface ITransformations<T> {
    Object getDimacs(T featureModel);
    Object getCNF(T featureModel);
    Object getUVL(T featureModel);
    Object getVelvet(T featureModel);
    Object getSxfml(T featureModel);
}
