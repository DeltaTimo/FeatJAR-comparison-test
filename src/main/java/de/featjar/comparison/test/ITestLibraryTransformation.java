package de.featjar.comparison.test;



public interface ITestLibraryTransformation {

    Result<String> getDimacs(String path);
    Result<String> getCNF(String path);
    Result<String> getUVL(String path);
    Result<String> getVelvet(String path);
    Result<String> getSxfml(String path);

}
