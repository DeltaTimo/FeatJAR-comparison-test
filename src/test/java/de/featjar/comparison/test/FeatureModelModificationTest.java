package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEModification;
import de.featjar.comparison.test.helper.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelModificationTest {
    private static final List<String> modelNames = Arrays.asList( //
            "FeatureModelModification/basic.xml"
    );
    private static final Map<String,String> featureModels = new HashMap<>();
    private static FeatureIDEBase baseOperations;
    private static FeatureIDEModification library1;
    private static FeatureIDEModification library2;

    private static String getPathFromResource(String resource) throws FileNotFoundException {
        final URL resourceURL = FeatureModelAnalysisTests.class.getClassLoader().getResource(resource);
        if (resourceURL == null) {
            throw new FileNotFoundException(resource);
        } else {
            return resourceURL.getPath().substring(1);
        }
    }

    @BeforeAll
    public static void setup() {
        baseOperations = new FeatureIDEBase();
        library1 = new FeatureIDEModification();
        library2 = new FeatureIDEModification();
        modelNames.forEach(module -> {
            try {
                featureModels.put(getXMLAsString(getPathFromResource(module)), module);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });
    }

    @Test
    public void testAddFeature() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.addFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.addFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testRemoveFeature() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.removeFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.removeFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testRemoveConstraint() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.removeConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.removeConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testAddConstraint() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.addConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.addConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testSlice() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.slice(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.slice(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testGeneralization() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.comparatorGeneralization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.comparatorGeneralization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testSpecialization() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(Result.get(() -> library1.comparatorSpecialization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), Result.get(() -> library2.comparatorSpecialization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    public static String getXMLAsString(String strXMLFilePath) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(strXMLFilePath)));
            String strLine;
            while( (strLine = reader.readLine()) != null ) {
                sb.append(strLine);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if( reader != null )
                    reader.close();

            }catch(Exception e) {}
        }
        return sb.toString();
    }
}
