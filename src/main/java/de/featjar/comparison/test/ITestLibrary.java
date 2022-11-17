package de.featjar.comparison.test;

import java.util.Set;

public interface ITestLibrary {
    Result<Boolean> isTautology(String filePath);
    Result<Boolean> isVoid(String filePath);
    Result<Set<String>> coreFeatures(String filePath);
    Result<Set<String>> deadFeatures(String filePath);
    Result<Set<String>> falseOptional(String filePath);
}
