package de.featjar.comparison.test;

import de.featjar.comparison.test.helper.featureide.FeatureIDEBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelBaseTests extends ATest{

    private static final List<String> MODEL_NAMES = Arrays.asList( //
            "FeatureModelAnalysis/basic.xml",
            "FeatureModelAnalysis/simple.xml",
            "FeatureModelAnalysis/car.xml",
            "FeatureModelAnalysis/hidden.xml"
    );
    private static FeatureIDEBase baseOperationsLib1;
    private static FeatureIDEBase baseOperationsLib2;
    private static  Map<String,String> featureModelsPaths = new HashMap<>();

    @BeforeAll
    public static void setup() {
        baseOperationsLib1 = new FeatureIDEBase();
        baseOperationsLib2 = new FeatureIDEBase();
        MODEL_NAMES.forEach(module -> {
            try {
                featureModelsPaths.put(getPathFromResource(module), getXMLAsString(getPathFromResource(module)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        });
    }

    @Test
    public void testLoad() {
         featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> assertEquals(baseOperationsLib1.load(entry.getKey()).toString(), baseOperationsLib2.load(entry.getKey()).toString()));
    }

    @Test
    public void testLoadFromSource() {
        featureModelsPaths
                .entrySet()
                .stream()
                .forEach(entry -> assertEquals(baseOperationsLib1.loadFromSource(entry.getValue(),entry.getKey()).toString(), baseOperationsLib2.loadFromSource(entry.getValue(),entry.getKey()).toString()));
    }
}
