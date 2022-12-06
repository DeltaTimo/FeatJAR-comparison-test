package de.featjar.comparison.test.helper.Wrapper;

public class LibraryObject {
    private String config;
    private String query;
    private Object featureModel;

    public LibraryObject(Object featureModel, String query, String config) {
        this.config = config;
        this.query = query;
        this.featureModel = featureModel;
    }

    public Object getFeatureModel() {
        return featureModel;
    }

    public String getConfig() {
        return config;
    }

    public String getQuery() {
        return query;
    }
}
