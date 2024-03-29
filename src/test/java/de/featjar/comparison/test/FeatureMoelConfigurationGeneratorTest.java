package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.Wrapper.LibraryObject;
import de.featjar.comparison.test.helper.Wrapper.WrapperLibrary;
import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEConfigurationGenerator;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An example usage of the FeatureIDE library and the FeatJAR library generating configurations.
 * includes the tests between the two libraries and the comparison of the test results.
 *
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.helper.IConfigurationGenerator
 * @see FeatureIDEConfigurationGenerator
 * @see org.junit.jupiter.api
 */
public class FeatureMoelConfigurationGeneratorTest extends ATest {

    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelConfigurationGenerator/basic.xml",
            "FeatureModelConfigurationGenerator/car.xml",
            "FeatureModelConfigurationGenerator/simple.xml",
            "FeatureModelConfigurationGenerator/test.xml"
    );

    private static final List<WrapperLibrary> featureModels = new ArrayList<>();
    private static FeatureIDEBase baseOperationsLib1;
    private static FeatureIDEBase baseOperationsLib2;
    private static FeatureIDEConfigurationGenerator library1;
    private static FeatureIDEConfigurationGenerator library2;

    @BeforeAll
    public static void setup() {
        // old lib
        baseOperationsLib1 = new FeatureIDEBase();
        library1 = new FeatureIDEConfigurationGenerator();
        // new lib
        baseOperationsLib2 = new FeatureIDEBase();
        library2 = new FeatureIDEConfigurationGenerator();

        MODEL_NAMES.forEach(module -> {
            LibraryObject libraryObjectFirst = new LibraryObject(baseOperationsLib1.load(getPathFromResource(module)), "","");
            LibraryObject libraryObjectSecond = new LibraryObject(baseOperationsLib2.load(getPathFromResource(module)), "","");
            featureModels.add(new WrapperLibrary(libraryObjectFirst, libraryObjectSecond));
        });
    }

    @Test
    public void testPairwiseConfigurationGenerator() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.pairwise((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.pairwise((IFeatureModel) featureModel.getObjectLib2().getFeatureModel()))));
    }

    @Test
    public void testAllConfigurationGenerator() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.all((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.all((IFeatureModel) featureModel.getObjectLib2().getFeatureModel()))));
    }

    @Test
    public void testIcplConfigurationGenerator() {
        featureModels.forEach(featureModel -> assertEquals(run(() -> library1.icpl((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.icpl((IFeatureModel) featureModel.getObjectLib2().getFeatureModel()))));
    }

    @Test
    public void testChvatalConfigurationGenerator() {
       featureModels.forEach(featureModel -> assertEquals(run(() -> library1.chvatal((IFeatureModel) featureModel.getObjectLib1().getFeatureModel())), run(() -> library2.chvatal((IFeatureModel) featureModel.getObjectLib2().getFeatureModel()))));
    }

}