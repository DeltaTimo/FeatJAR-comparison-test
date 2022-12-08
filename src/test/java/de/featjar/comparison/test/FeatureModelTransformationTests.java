package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDETransformation;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelTransformationTests extends ATest{

    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelTransformation/model.xml"
    );

    private static FeatureIDETransformation library1;
    private static FeatureIDETransformation library2;
    private static FeatureIDEBase baseOperations;
    private static final List<IFeatureModel> featureModels = new ArrayList<>();


    @BeforeAll
    public static void setup() {
        baseOperations = new FeatureIDEBase();
        library1 = new FeatureIDETransformation();
        library2 = new FeatureIDETransformation();
        MODEL_NAMES.forEach(module -> {
            featureModels.add(baseOperations.load(getPathFromResource(module)));
        });
    }

    @Test
    public void testDimacs() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.getDimacs(featureModel)), run(() -> library2.getDimacs(featureModel))));
    }

    @Test
    public void testCNF() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.getCNF(featureModel)), run(() -> library2.getCNF(featureModel))));
    }

    @Test
    public void testUVL() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.getUVL(featureModel)), run(() -> library2.getUVL(featureModel))));
    }

    @Test
    public void tetSXFML() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.getSxfml(featureModel)), run(() -> library2.getSxfml(featureModel))));
    }

    @Test
    public void testVelvet() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.getVelvet(featureModel)), run(() -> library2.getVelvet(featureModel))));
    }
}