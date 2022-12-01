package de.featjar.comparison.test.helper;

public interface ITransformations<T> {
    Result<String> getDimacs(T featureModel);
    Result<String> getCNF(T featureModel);
    Result<String> getUVL(T featureModel);
    Result<String> getVelvet(T featureModel);
    Result<String> getSxfml(T featureModel);
}
