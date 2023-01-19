package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEModification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library and the FeatJAR library for modifications on feature-model.
 * includes the tests between the two libraries and the comparison of the test results.
 *
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.helper.IModification
 * @see FeatureIDEModification
 * @see org.junit.jupiter.api
 */
public class FeatureModelModificationTest extends ATest{
    private static final String ADDITONAL_INFO = "FeatureModelModification/additional.txt";
    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelModification/basic.xml"
    );
    private static final Map<String,String> featureModels = new HashMap<>();
    private static FeatureIDEBase baseOperations;
    private static FeatureIDEModification library1;
    private static FeatureIDEModification library2;

    @BeforeAll
    public static void setup() {
        baseOperations = new FeatureIDEBase();
        library1 = new FeatureIDEModification();
        library2 = new FeatureIDEModification();
        MODEL_NAMES.forEach(module -> {
            featureModels.put(getXMLAsString(getPathFromResource(module)), getPathFromResource(module));
            library1.getDataForModification(getPathFromResource(ADDITONAL_INFO));
            library2.getDataForModification(getPathFromResource(ADDITONAL_INFO));
        });
    }

    @Test
    public void testAddFeature() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.addFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName())), run(() -> library2.addFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName()))));
    }

    @Test
    public void testRemoveFeature() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.removeFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), run(() -> library2.removeFeature(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testRemoveConstraint() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.removeConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()))), run(() -> library2.removeConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue())))));
    }

    @Test
    public void testAddConstraint() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.addConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName())), run(() -> library2.addConstraint(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName()))));
    }

    @Test
    public void testSlice() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.slice(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName())), run(() -> library2.slice(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName()))));
    }

    @Test
    public void testGeneralization() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.comparatorGeneralization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName())), run(() -> library2.comparatorGeneralization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName()))));
    }

    @Test
    public void testSpecialization() {
        featureModels.entrySet()
                .stream()
                .forEach(featureModel -> assertEquals(run(() -> library1.comparatorSpecialization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName())), run(() -> library2.comparatorSpecialization(baseOperations.loadFromSource(featureModel.getKey(), featureModel.getValue()), new File (featureModel.getValue()).getName()))));
    }
}
