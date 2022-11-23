package de.featjar.comparison.test;

import org.stringtemplate.v4.ST;

import java.util.List;
import java.util.Set;

public interface ITestLibrary {
    Result<Boolean> isTautology(String filePath, String[] parameters);
    Result<Boolean> isVoid(String filePath);
    Result<Set<String>> coreFeatures(String filePath);
    Result<Set<String>> deadFeatures(String filePath);
    Result<Set<String>> falseOptional(String filePath);
    Result<Set<String>> redundantConstraints(String filePath);
    Result<List<Set<String>>> atomicSets(String filePath);
    Result<Set<String>> indeterminedHiddenFeatures(String filePath);
}
