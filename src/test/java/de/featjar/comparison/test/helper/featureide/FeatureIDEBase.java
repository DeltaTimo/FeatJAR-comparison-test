package de.featjar.comparison.test.helper.featureide;

import de.featjar.comparison.test.helper.IBase;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelIO;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;
import org.prop4j.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FeatureIDEBase implements IBase<IFeatureModel, Node> {

    @Override
    public IFeatureModel load(String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        Path path = Paths.get(filepath);
        return FeatureModelManager.load(path);
    }

    @Override
    public IFeatureModel loadFromSource(String content, String filepath) {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
        FeatureModelIO featureModelIO = FeatureModelIO.getInstance();
        return featureModelIO.loadFromSource(content, Paths.get(filepath));
    }

    @Override
    public Node createQueryImpl(String feature1, String feature2) {
        return new Implies(new Literal(feature1), new Literal(feature2));
    }

    @Override
    public Node createQueryAndNot(String feature1, String feature2) {
        return new And(new Literal(feature1), new Not(new Literal(feature2)));
    }

    @Override
    public String loadConfiguration(String filepath) {
        String content = null;
        try {
            FileInputStream fis = new FileInputStream(filepath);
            byte[] buffer = new byte[10];
            StringBuilder sb = new StringBuilder();
            while (fis.read(buffer) != -1) {
                sb.append(new String(buffer));
                buffer = new byte[10];
            }
            fis.close();
            content = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
