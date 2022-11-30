package de.featjar.comparison.test;


import de.ovgu.featureide.fm.core.base.IFeatureModel;

public interface ITransformations {
    Result<String> getDimacs(IFeatureModel featureModel);
    Result<String> getCNF(IFeatureModel featureModel);
    Result<String> getUVL(IFeatureModel featureModel);
    Result<String> getVelvet(IFeatureModel featureModel);
    Result<String> getSxfml(IFeatureModel featureModel);
}
