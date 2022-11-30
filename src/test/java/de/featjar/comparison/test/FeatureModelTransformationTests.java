package de.featjar.comparison.test;

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

    private static FeatureIDELibrary library1;
    private static FeatureIDELibrary library2;
    private static final List<IFeatureModel> featureModels = new ArrayList<>();


    @BeforeAll
    public static void setup() {
        library1 = new FeatureIDELibrary();
        library2 = new FeatureIDELibrary();
        modelNames.forEach(module -> {
            try {
                featureModels.add(loadModel(getPathFromResource(module)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private static IFeatureModel loadModel(String filePath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filePath);
        return FeatureModelManager.load(path);
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