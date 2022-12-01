package de.featjar.comparison.test.helper;

public class WrapperFeatureModels {
    private Object featureModel1;
    private Object featureModel2;

    public WrapperFeatureModels(Object featureModel1, Object featureModel2) {
        this.featureModel1 = featureModel1;
        this.featureModel2 = featureModel2;
    }

    public Object getFeatureModel1() {
        return featureModel1;
    }

    public Object getFeatureModel2() {
        return featureModel2;
    }

    public void setFeatureModel1(Object featureModel1) {
        this.featureModel1 = featureModel1;
    }

    public void setFeatureModel2(Object featureModel2) {
        this.featureModel2 = featureModel2;
    }
}
