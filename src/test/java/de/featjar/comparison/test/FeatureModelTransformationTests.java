package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDETransformation;
import de.featjar.comparison.test.helper.Result;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelTransformationTests {

    private static final List<String> modelNames = Arrays.asList( //
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
        modelNames.forEach(module -> {
            try {
                featureModels.add(baseOperations.load(getPathFromResource(module)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });
    }

    private static String getPathFromResource(String resource) throws FileNotFoundException {
        final URL resourceURL = FeatureModelTransformationTests.class.getClassLoader().getResource(resource);
        if (resourceURL == null) {
            throw new FileNotFoundException(resource);
        } else {
            return resourceURL.getPath().substring(1);
        }
    }

    @Test
    public void testDimacs() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.getDimacs(featureModel)), Result.get(() -> library2.getDimacs(featureModel))));
    }

    @Test
    public void testCNF() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.getCNF(featureModel)), Result.get(() -> library2.getCNF(featureModel))));
    }

    @Test
    public void testUVL() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.getUVL(featureModel)), Result.get(() -> library2.getUVL(featureModel))));
    }

    @Test
    public void tetSXFML() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.getSxfml(featureModel)), Result.get(() -> library2.getSxfml(featureModel))));
    }

    @Test
    public void testVelvet() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.getVelvet(featureModel)), Result.get(() -> library2.getVelvet(featureModel))));
    }
}