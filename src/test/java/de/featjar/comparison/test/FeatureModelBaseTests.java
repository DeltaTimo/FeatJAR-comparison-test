package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featjar.FeatJARBase;
import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelBaseTests extends ATest{

    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelAnalysis/basic.xml",
            "FeatureModelAnalysis/simple.xml",
            "FeatureModelAnalysis/car.xml",
            "FeatureModelAnalysis/hidden.xml"
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
    public void testCompareLoadedFormulars() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> {
                    Object a = baseOperationsLib1.load(entry.getKey());
                    Object b = baseOperationsLib2.load(entry.getKey());
                    assertEquals(baseOperationsLib1.getFormula(a), baseOperationsLib2.getFormula(b));
                });
    }


    @Test
    public void testLoadFromSource() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> assertEquals(baseOperationsLib1.loadFromSource(entry.getValue(),entry.getKey()).toString(), baseOperationsLib2.loadFromSource(entry.getValue(),entry.getKey()).toString()));
    }
}