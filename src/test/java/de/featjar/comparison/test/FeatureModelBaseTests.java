package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
    private static FeatureIDEBase baseOperationsLib2;
    private static  Map<String,String> featureModelsPaths = new HashMap<>();

    @BeforeAll
    public static void setup() {
        baseOperationsLib1 = new FeatureIDEBase();
        baseOperationsLib2 = new FeatureIDEBase();
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
                    Executable execute1 = () -> baseOperationsLib1.load(entry.getKey());
                    assertDoesNotThrow(execute1);
                    Executable execute2 = () -> baseOperationsLib2.load(entry.getKey());
                    assertDoesNotThrow(execute2);
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
    public void testLoadFromSource() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> assertEquals(baseOperationsLib1.loadFromSource(entry.getValue(),entry.getKey()).toString(), baseOperationsLib2.loadFromSource(entry.getValue(),entry.getKey()).toString()));
    }
}
