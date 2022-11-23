package de.featjar.comparison.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureModelTransformationTests {

    ITestLibraryTransformation library1;
    ITestLibraryTransformation library2;

    private String getPathFromResource(String resource) throws FileNotFoundException {
        final URL resourceURL = getClass().getClassLoader().getResource(resource);
        if (resourceURL == null) {
            throw new FileNotFoundException(resource);
        } else {
            return resourceURL.getPath().substring(1);
        }
    }

    @BeforeEach
    public void setup() {
        library1 = new FeatureIDETransformation();
        library2 = new FeatureIDETransformation();
    }

    @Test
    public void testDimacs() throws FileNotFoundException {
        String resource = getPathFromResource("FeatureModelTransformation/model.xml");
        assertEquals(Result.get(() -> library1.getDimacs(resource)), Result.get(() -> library2.getDimacs(resource)));
    }

    @Test
    public void testCNF() throws FileNotFoundException {
        String resource = getPathFromResource("FeatureModelTransformation/model.xml");
        assertEquals(Result.get(() -> library1.getCNF(resource)), Result.get(() -> library2.getCNF(resource)));
    }

    @Test
    public void testUVL() throws FileNotFoundException {
        String resource = getPathFromResource("FeatureModelTransformation/model.xml");
        assertEquals(Result.get(() -> library1.getUVL(resource)), Result.get(() -> library2.getUVL(resource)));
    }

    @Test
    public void tetSXFML() throws FileNotFoundException {
        String resource = getPathFromResource("FeatureModelTransformation/model.xml");
        assertEquals(Result.get(() -> library1.getSxfml(resource)), Result.get(() -> library2.getSxfml(resource)));
    }

    @Test
    public void testVelvet() throws FileNotFoundException {
        String resource = getPathFromResource("FeatureModelTransformation/model.xml");
        assertEquals(Result.get(() -> library1.getVelvet(resource)), Result.get(() -> library2.getVelvet(resource)));
    }
}