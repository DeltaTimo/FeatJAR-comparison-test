package de.featjar.comparison.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

public abstract class ATest {
    public static void setup() {}
    static String getPathFromResource(String resource) throws FileNotFoundException {
        final URL resourceURL = FeatureModelAnalysisTests.class.getClassLoader().getResource(resource);
        if (resourceURL == null) {
            throw new FileNotFoundException(resource);
        } else {
            return resourceURL.getPath().substring(1);
        }
    }

    public static String getXMLAsString(String strXMLFilePath) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(strXMLFilePath)));
            String strLine;
            while( (strLine = reader.readLine()) != null ) {
                sb.append(strLine);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if( reader != null )
                    reader.close();

            }catch(Exception e) {}
        }
        return sb.toString();
    }
}