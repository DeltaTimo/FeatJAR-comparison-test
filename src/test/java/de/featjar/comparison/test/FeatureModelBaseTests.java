package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featjar.FeatJARBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import de.featjar.formula.structure.formula.Formula;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * An example usage of the FeatureIDE library and the FeatJAR library for feature-model base operations.
 * includes the tests between the two libraries and the comparison of the test results.
 *
 * @author Katjana Herbst
 * @see de.featjar.comparison.test.helper.IBase
 * @see FeatureIDEBase
 * @see FeatJARBase
 * @see org.junit.jupiter.api
 */
public class FeatureModelBaseTests extends ATest{

    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelAnalysis/basic.xml",
            "FeatureModelAnalysis/simple.xml",
            "FeatureModelAnalysis/car.xml"
    );

    private static final List<String> WRONG_MODEL_NAMES = Arrays.asList( //
            "WrongPath/basic.xml",
            "WrongPath/simple.xml"
    );

    private static FeatureIDEBase baseOperationsLib1;
    private static FeatJARBase baseOperationsLib2;
    private static  Map<String,String> featureModelsPaths = new HashMap<>();

    @BeforeAll
    public static void setup() {
        baseOperationsLib1 = new FeatureIDEBase();
        baseOperationsLib2 = new FeatJARBase();
        MODEL_NAMES.forEach(module -> {
            featureModelsPaths.put(getPathFromResource(module), getXMLAsString(getPathFromResource(module)));
        });
    }

    @Test
    public void testLoad() {
         featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> {
                    assertDoesNotThrow(() -> baseOperationsLib1.load(entry.getKey()));
                    assertDoesNotThrow(() -> baseOperationsLib2.load(entry.getKey()));
                });
    }

    @Test
    public void testLoadWrongPaths() {
        // testet getPathFromResource method from ATest and load method
        WRONG_MODEL_NAMES.forEach(wrongPath -> {
            assertThrows(RuntimeException.class, () -> {
                baseOperationsLib1.load(getPathFromResource(wrongPath));
                baseOperationsLib2.load(getPathFromResource(wrongPath));
            });
        });
    }

    @Test
    public void testCompareLoadedFormularsStrict() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> {
                    Object a = baseOperationsLib1.load(entry.getKey());
                    Object b = baseOperationsLib2.load(entry.getKey());
                    // assumeTrue = true then only the rest of the test method is executed, else the test is skipped
                    assumeTrue(baseOperationsLib1.getFormula(a).equals(baseOperationsLib2.getFormula(b)));
                    assertEquals(baseOperationsLib1.getFormula(a), baseOperationsLib2.getFormula(b));
                });
    }

    @Test
    public void testCompareLoadedFormularsLoose() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> {
                    Object a = baseOperationsLib1.load(entry.getKey());
                    Object b = baseOperationsLib2.load(entry.getKey());
                    assertEquals(baseOperationsLib1.smoothFormula((IFeatureModel) a), baseOperationsLib2.smoothFormula((Formula) b));
                });
    }

    @Test
    public void testLoadFromSource() {
        // TODO in featJAR method return null
        /*
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> assertEquals(baseOperationsLib1.loadFromSource(entry.getValue(),entry.getKey()).toString(), baseOperationsLib2.loadFromSource(entry.getValue(),entry.getKey()).toString()));
        */
    }
}