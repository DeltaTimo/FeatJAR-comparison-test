package de.featjar.comparison.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.concurrent.Callable;

public abstract class ATest {
    //TODO test getPathFromResource
    public static void setup() {}
    static String getPathFromResource(String resource) {
        File file = new File(FeatureModelAnalysisTests.class.getClassLoader().getResource(resource).getPath());
        if (file == null) {
            try {
                throw new FileNotFoundException(resource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        } else {
            return file.toPath().toString();
        }
    }
    //TODO test getXMLAsString
    public static String getXMLAsString(String strXMLFilePath) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(strXMLFilePath));
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

    static Object run(Callable<?> f)
    {
        try {
            return f.call();
        } catch (final Exception e) {
            return e;
        }
    }
}