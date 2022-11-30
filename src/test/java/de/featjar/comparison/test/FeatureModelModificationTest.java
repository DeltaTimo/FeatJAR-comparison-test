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

public class FeatureModelModificationTest {
    private static final List<String> modelNames = Arrays.asList( //
            "FeatureModelModification/basic.xml"
    );
    private static final List<IFeatureModel> featureModels = new ArrayList<>();
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
        library1 = new FeatureIDEModification();
        library2 = new FeatureIDEModification();
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
    @Test
    public void testAddFeature() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.addFeature(featureModel.clone())), Result.get(() -> library2.addFeature(featureModel.clone()))));
    }

    @Test
    public void testRemoveFeature() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.removeFeature(featureModel.clone())), Result.get(() -> library2.removeFeature(featureModel.clone()))));
    }

    @Test
    public void testRemoveConstraint() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.removeConstraint(featureModel.clone())), Result.get(() -> library2.removeConstraint(featureModel.clone()))));
    }

    @Test
    public void testAddConstraint() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.addConstraint(featureModel.clone())), Result.get(() -> library2.addConstraint(featureModel.clone()))));
    }

    @Test
    public void testSlice() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.slice(featureModel.clone())), Result.get(() -> library2.slice(featureModel.clone()))));
    }

    @Test
    public void testGeneralization() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.comparatorGeneralization(featureModel.clone())), Result.get(() -> library2.comparatorGeneralization(featureModel.clone()))));
    }

    @Test
    public void testSpecialization() {
        featureModels.forEach(featureModel -> assertEquals(Result.get(() -> library1.comparatorSpecialization(featureModel.clone())), Result.get(() -> library2.comparatorSpecialization(featureModel.clone()))));
    }
}
